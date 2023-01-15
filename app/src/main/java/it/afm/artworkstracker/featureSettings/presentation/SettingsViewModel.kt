package it.afm.artworkstracker.featureSettings.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.afm.artworkstracker.core.domain.useCase.GetArtworksIdsUseCase
import it.afm.artworkstracker.featureSettings.domain.useCase.DeleteArtworksUseCase
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val deleteArtworksUseCase: DeleteArtworksUseCase,
    getArtworksIdsUseCase: GetArtworksIdsUseCase
): ViewModel() {

    private val _uiState = mutableStateOf(SettingsUiState())
    val uiState: State<SettingsUiState> = _uiState

    init {
        getArtworksIdsUseCase().onEach {
            _uiState.value = _uiState.value.copy(
                isEndVisitEnabled = it.isNotEmpty()
            )
        }.launchIn(viewModelScope)
    }

    fun onEvent(event: SettingsEvent) {
        when(event) {
            is SettingsEvent.DeleteArtworks -> {
                viewModelScope.launch {
                    deleteArtworksUseCase()
                }
            }
        }
    }
}