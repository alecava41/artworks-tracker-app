package it.afm.artworkstracker.featureMuseumMap.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.afm.artworkstracker.featureMuseumMap.domain.useCase.GetCloserBeaconsUseCase
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MuseumMapViewModel @Inject constructor(
    private val getCloserBeaconsUseCase: GetCloserBeaconsUseCase
) : ViewModel() {

    init {
        viewModelScope.launch {
            getCloserBeaconsUseCase().onEach {
                _museumMapState.value = museumMapState.value.copy(
                    closerBeacon = it
                )
            }.launchIn(viewModelScope)
        }
    }

    private val _museumMapState = mutableStateOf(MuseumMapState())
    val museumMapState: State<MuseumMapState> = _museumMapState

    fun onEvent(event: MuseumMapEvent) {
        when(event) {
            is MuseumMapEvent.ResumeTour -> getCloserBeaconsUseCase.startListeningForBeacons()
            is MuseumMapEvent.PauseTour -> getCloserBeaconsUseCase.stopListeningForBeacons()
        }
    }
}