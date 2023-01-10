package it.afm.artworkstracker.featureMuseumMap.presentation

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.afm.artworkstracker.featureMuseumMap.domain.model.ArtworkInfo
import it.afm.artworkstracker.featureMuseumMap.domain.model.Beacon
import it.afm.artworkstracker.featureMuseumMap.domain.model.Room
import it.afm.artworkstracker.featureMuseumMap.domain.useCase.GetArtworksIdsUseCase
import it.afm.artworkstracker.featureMuseumMap.domain.useCase.GetCloserBeaconsUseCase
import it.afm.artworkstracker.featureMuseumMap.domain.useCase.GetRoomUseCase
import it.afm.artworkstracker.featureMuseumMap.domain.util.ArtworkType
import it.afm.artworkstracker.featureMuseumMap.domain.util.PerimeterEntity
import it.afm.artworkstracker.featureMuseumMap.domain.util.Side
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
    getArtworksIdsUseCase: GetArtworksIdsUseCase
) : ViewModel() {

    var baseUrl: String? = null
        private set

    private val _museumMapState = mutableStateOf(MuseumMapState())
    val museumMapState: State<MuseumMapState> = _museumMapState

    private val _environmentState = mutableStateOf(EnvironmentState())
    val environmentState: State<EnvironmentState> = _environmentState

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var currentClosestBeacon: Beacon? = null

    private var knownArtworks = listOf<UUID>()

    init {
        // TODO: add bluetooth enabled check + broadcast receiver to get information about bluetooth state
        // TODO: add wifi enabled check + broadcast receiver to get information about wifi state
        // TODO: add location enabled check (only for older SDK) + broadcast receiver if it disabled!!

        // TODO: add "walking" path to the map (passing through artworks)
        // TODO: add TTS for telling the path to reach the next artwork (starting from a specific artwork)
        // TODO: check if the first language of the device is one the known, otherwise set TTS to english
        // TODO: add "Bottom Navigation Bar" (entries: MAP, ARTWORKS_LIST)
        // TODO: add "Top App Bar" (App title (?) + action_button (close_visit, ?))
        // TODO: check if it is possible to "close" an artwork's card without using navController

        getCloserBeaconsUseCase().onEach {
            val isNewClosestBeacon = it != null && (currentClosestBeacon == null || currentClosestBeacon!!.id != it.id)

            if (!baseUrl.isNullOrBlank() && isNewClosestBeacon) {
                currentClosestBeacon = it
                val isBeaconInCurrentRoom = _museumMapState.value.room?.artworks?.find { artwork ->
                    artwork.beacon == it!!.id
                } != null

                if (!isBeaconInCurrentRoom) {
                    val room = getRoomUseCase(it!!.id, baseUrl!!)

                    if (room != null && room != _museumMapState.value.room) {
                        room.artworks.forEach { artwork ->
                            artwork.visited = knownArtworks.contains(artwork.beacon)
                        }

                        Log.i(TAG, "room = $room")

                        _museumMapState.value = _museumMapState.value.copy(
                            room = room,
                            currentBeaconRanged = it,
                            lastBeaconRanged = _museumMapState.value.currentBeaconRanged
                        )
                    } else {
                        _museumMapState.value = _museumMapState.value.copy(
                            room = null,
                        )
                    }
                } else {
                    _museumMapState.value = _museumMapState.value.copy(
                        currentBeaconRanged = it,
                        lastBeaconRanged = _museumMapState.value.currentBeaconRanged
                    )
                }

                val isArtworkAlreadyVisited =
                    _museumMapState.value.room?.artworks?.find { artwork -> artwork.beacon == it!!.id }?.visited ?: false

                if (isArtworkAlreadyVisited) _eventFlow.emit(UiEvent.NewCloserBeaconAlreadyVisited(uuid = it!!.id))
                else _eventFlow.emit(UiEvent.NewCloserBeacon(uuid = it!!.id))
            }
        }.launchIn(viewModelScope)

        getArtworksIdsUseCase().onEach {
            knownArtworks = it

            _museumMapState.value.room?.artworks?.forEach { artwork ->
                artwork.visited = knownArtworks.contains(artwork.beacon)
            }

        }.launchIn(viewModelScope)
    }

    fun onEvent(event: MuseumMapEvent) {
        when (event) {
            is MuseumMapEvent.ResumeTour -> getCloserBeaconsUseCase.startListeningForBeacons()
            is MuseumMapEvent.PauseTour -> getCloserBeaconsUseCase.stopListeningForBeacons()
            is MuseumMapEvent.BackendServerDiscovered -> baseUrl = "http://${event.ip}:${event.port}"
            is MuseumMapEvent.ViewArtwork -> {
                viewModelScope.launch {
                    val isArtworkAlreadyVisited = knownArtworks.contains(event.id)

                    _museumMapState.value = _museumMapState.value.copy(
                        lastBeaconRanged = _museumMapState.value.currentBeaconRanged
                    )

                    if (isArtworkAlreadyVisited)
                        _eventFlow.emit(UiEvent.ArtworkAlreadyVisitedClicked(event.id))
                    else
                        _eventFlow.emit(UiEvent.ArtworkNotVisitedClicked)
                }
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
            posX = 500,
            posY = 50
        )
    ),
    walls = arrayListOf(),
    id = 3
)