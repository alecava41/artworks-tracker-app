package it.afm.artworkstracker.featureVisitedArtworksList.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.afm.artworkstracker.featureVisitedArtworksList.domain.useCase.GetVisitedArtworksListUseCase
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class VisitedArtworksListViewModel @Inject constructor(
    getVisitedArtworksListUseCase: GetVisitedArtworksListUseCase
) : ViewModel() {
    private val _uiState = mutableStateOf(VisitedArtworksListState())
    val uiState: State<VisitedArtworksListState> = _uiState

    init{
        getVisitedArtworksListUseCase().onEach {
            _uiState.value = _uiState.value.copy(
                visitedArtworksList = it
            )
        }.launchIn(viewModelScope)
    }
}