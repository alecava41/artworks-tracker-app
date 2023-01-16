package it.afm.artworkstracker.featureMuseumMap.presentation

import android.app.Application
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.afm.artworkstracker.featureMuseumMap.domain.model.ArtworkBeacon
import it.afm.artworkstracker.featureMuseumMap.domain.model.Beacon
import it.afm.artworkstracker.core.domain.useCase.GetArtworksIdsUseCase
import it.afm.artworkstracker.featureMuseumMap.domain.model.ArtworkInfo
import it.afm.artworkstracker.featureMuseumMap.domain.model.Room
import it.afm.artworkstracker.featureMuseumMap.domain.useCase.GetCloserBeaconsUseCase
import it.afm.artworkstracker.featureMuseumMap.domain.useCase.GetRoomUseCase
import it.afm.artworkstracker.featureMuseumMap.domain.util.ArtworkType
import it.afm.artworkstracker.featureMuseumMap.domain.util.PerimeterEntity
import it.afm.artworkstracker.featureMuseumMap.domain.util.Side
import it.afm.artworkstracker.util.LanguageUtil
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MuseumMapViewModel @Inject constructor(
    private val getCloserBeaconsUseCase: GetCloserBeaconsUseCase,
    private val getRoomUseCase: GetRoomUseCase,
    getArtworksIdsUseCase: GetArtworksIdsUseCase,
    app: Application
) : AndroidViewModel(app) {

    var baseUrl: String? = null
        private set

    private var isScanning = false

    private val _museumMapState = mutableStateOf(MuseumMapState())
    val museumMapState: State<MuseumMapState> = _museumMapState

    private val _environmentState = mutableStateOf(EnvironmentState())
    val environmentState: State<EnvironmentState> = _environmentState

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var currentClosestBeacon: Beacon? = null

    private var knownArtworks = listOf<UUID>()

    private val lan = String().apply {
        val firstLan = getApplication<Application>().applicationContext.resources.configuration.locales[0]

        if (LanguageUtil.supportedLanguages.contains(firstLan.language)) firstLan.language
        else Locale.ENGLISH.language
    }

    init {
        // TODO: add "Top App Bar" (equal to bottomAppBar (color)))

        // TODO: redefine "error" screens (only view side)
        // TODO: refine visited list view (padding, bold title, ...)
        // TODO: colors

        getCloserBeaconsUseCase().onEach {
            val isNewClosestBeacon = it != null && (currentClosestBeacon == null || currentClosestBeacon!!.id != it.id)

            if (!baseUrl.isNullOrBlank() && isNewClosestBeacon) {
                currentClosestBeacon = it
                val isBeaconInCurrentRoom = _museumMapState.value.room?.artworks?.find { artwork ->
                    artwork.beacon == it!!.id
                } != null

                if (!isBeaconInCurrentRoom) {
                    val room = getRoomUseCase(it!!.id, baseUrl!!, lan)

                    if (room != null && room != _museumMapState.value.room) {
                        room.artworks.forEach { artwork ->
                            artwork.visited = knownArtworks.contains(artwork.beacon)
                        }

                        Log.i(TAG, "room = $room")

                        _museumMapState.value = _museumMapState.value.copy(
                            room = room,
                        )
                    } else {
                        _museumMapState.value = _museumMapState.value.copy(
                            room = null,
                        )
                    }
                }
                val newClosestArtworkItem = ArtworkBeacon(
                    id = it!!.id,
                    direction = _museumMapState.value.room?.artworks?.find { artwork -> artwork.beacon == it.id }?.direction ?: ""
                )

                _museumMapState.value = _museumMapState.value.copy(
                    currentArtwork = newClosestArtworkItem,
                    lastArtwork = _museumMapState.value.currentArtwork
                )

                val isArtworkAlreadyVisited =
                    _museumMapState.value.room?.artworks?.find { artwork -> artwork.beacon == it.id }?.visited ?: false

                if (isArtworkAlreadyVisited) _eventFlow.emit(UiEvent.NewCloserBeaconAlreadyVisited(uuid = it.id))
                else _eventFlow.emit(UiEvent.NewCloserBeacon(uuid = it.id))
            }
        }.launchIn(viewModelScope)

        getArtworksIdsUseCase().onEach {
            knownArtworks = it

            _museumMapState.value.room?.artworks?.forEach { artwork ->
                artwork.visited = knownArtworks.contains(artwork.beacon)
            }

            _environmentState.value = _environmentState.value.copy(
                isTourStarted = it.isNotEmpty()
            )

        }.launchIn(viewModelScope)
    }

    fun onEvent(event: MuseumMapEvent) {
        when (event) {
            is MuseumMapEvent.StartTour -> {
                _environmentState.value = _environmentState.value.copy(
                    isTourStarted = true
                )

                onEvent(MuseumMapEvent.ResumeTour)
            }
            is MuseumMapEvent.ResumeTour -> {
                if (!isScanning) {
                    getCloserBeaconsUseCase.startListeningForBeacons()
                    isScanning = true
                }
            }
            is MuseumMapEvent.PauseTour -> {
                if (isScanning) {
                    getCloserBeaconsUseCase.stopListeningForBeacons()
                    isScanning = false
                }
            }
            is MuseumMapEvent.SpeechStatus -> {
                _museumMapState.value = _museumMapState.value.copy(
                    isAudioEnabled = event.isSpeaking
                )
            }
            is MuseumMapEvent.ViewArtwork -> {
                viewModelScope.launch {
                    val isArtworkAlreadyVisited = knownArtworks.contains(event.id)

                    _museumMapState.value = _museumMapState.value.copy(
                        lastArtwork = _museumMapState.value.currentArtwork
                    )

                    if (isArtworkAlreadyVisited) _eventFlow.emit(UiEvent.ArtworkAlreadyVisitedClicked(event.id))
                    else _eventFlow.emit(UiEvent.ArtworkNotVisitedClicked)
                }
            }
            is MuseumMapEvent.BackendServerLost -> {
                baseUrl = null

                _environmentState.value = _environmentState.value.copy(
                    isWifiEnabled = false
                )
            }
            is MuseumMapEvent.BackendServerDiscovered -> {
                baseUrl = "http://${event.ip}:${event.port}"

                _environmentState.value = _environmentState.value.copy(
                    isWifiEnabled = true
                )
            }
            is MuseumMapEvent.WifiConnectionAvailable -> {
                _environmentState.value = _environmentState.value.copy(
                    isWifiEnabled = baseUrl != null
                )
            }
            is MuseumMapEvent.WifiConnectionNotAvailable -> {
                baseUrl = null

                _environmentState.value = _environmentState.value.copy(
                    isWifiEnabled = false
                )
            }
            is MuseumMapEvent.BluetoothAvailable -> {
                _environmentState.value = _environmentState.value.copy(
                    isBluetoothEnabled = true
                )
            }
            is MuseumMapEvent.BluetoothNotAvailable -> {
                _environmentState.value = _environmentState.value.copy(
                    isBluetoothEnabled = false
                )
            }
            is MuseumMapEvent.LocationAvailable -> {
                _environmentState.value = _environmentState.value.copy(
                    isLocationEnabled = true
                )
            }
            is MuseumMapEvent.LocationNotAvailable -> {
                _environmentState.value = _environmentState.value.copy(
                    isLocationEnabled = false
                )
            }
        }
    }

    companion object {
        const val TAG = "MuseumMapViewModel"
    }
}

val defaultRoom = Room(
    name = "King's bedroom",
    perimeter = listOf(
        Triple(PerimeterEntity.MOVE, 0, 0),
        Triple(PerimeterEntity.LINE, 0, 125),
        Triple(PerimeterEntity.MOVE, 0, 375),
        Triple(PerimeterEntity.LINE, 0, 500),
        Triple(PerimeterEntity.LINE, 500, 500),
        Triple(PerimeterEntity.LINE, 500, 1000),
        Triple(PerimeterEntity.LINE, 625, 1000),
        Triple(PerimeterEntity.MOVE, 875, 1000),
        Triple(PerimeterEntity.LINE, 1000, 1000),
        Triple(PerimeterEntity.LINE, 1000, 0),
        Triple(PerimeterEntity.LINE, 0, 0)
    ),
    artworks = listOf(
        ArtworkInfo(
            id = 1,
            beacon = UUID.randomUUID(),
            starred = true,
            visited = true,
            type = ArtworkType.PICTURE,
            side = Side.LEFT,
            direction = "",
            posX = 50,
            posY = 50
        ),
        ArtworkInfo(
            id = 2,
            beacon = UUID.randomUUID(),
            starred = true,
            visited = true,
            type = ArtworkType.SCULPTURE,
            side = Side.DOWN,
            direction = "",
            posX = 250,
            posY = 50
        ),
        ArtworkInfo(
            id = 3,
            beacon = UUID.randomUUID(),
            starred = true,
            visited = true,
            side = Side.RIGHT,
            type = ArtworkType.PICTURE,
            direction = "",
            posX = 500,
            posY = 50
        )
    ),
    walls = arrayListOf(),
    id = 3,
    starredPath = listOf()
)