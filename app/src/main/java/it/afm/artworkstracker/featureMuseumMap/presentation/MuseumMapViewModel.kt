package it.afm.artworkstracker.featureMuseumMap.presentation

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.afm.artworkstracker.featureMuseumMap.domain.useCase.GetCloserBeaconsUseCase
import it.afm.artworkstracker.featureMuseumMap.domain.useCase.GetRoomUseCase
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class MuseumMapViewModel @Inject constructor(
    private val getCloserBeaconsUseCase: GetCloserBeaconsUseCase,
    private val getRoomUseCase: GetRoomUseCase
) : ViewModel() {

    private var baseUrl: String? = null
    private val _museumMapState = mutableStateOf(MuseumMapState())
    val museumMapState: State<MuseumMapState> = _museumMapState

    init {
       getCloserBeaconsUseCase().onEach {
           if (!baseUrl.isNullOrBlank()) {
               val room = getRoomUseCase(it.id, baseUrl!!)

               Log.i(TAG, "Room = $room")

               if (room != null) {
                   _museumMapState.value = museumMapState.value.copy(
                       room = room,
                       closestBeacon = it
                   )
               }
           }
       }.launchIn(viewModelScope)
    }

    fun onEvent(event: MuseumMapEvent) {
        when(event) {
            is MuseumMapEvent.ResumeTour -> getCloserBeaconsUseCase.startListeningForBeacons()
            is MuseumMapEvent.PauseTour -> getCloserBeaconsUseCase.stopListeningForBeacons()
            is MuseumMapEvent.BackendServerDiscovered -> {
                Log.i(TAG,"Backend server discovered: ${event.ip}:${event.port}")
                // TODO: move traffic to https?
                baseUrl = "http://${event.ip}:${event.port}"
            }
        }
    }

    companion object {
        const val TAG = "MuseumMapViewModel"
    }
}