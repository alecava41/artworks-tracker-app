package it.afm.artworkstracker.featureArtwork.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.afm.artworkstracker.featureArtwork.domain.useCase.GetArtworkUseCase
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ArtworkViewModel @Inject constructor(
    private val artworkUseCase: GetArtworkUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    // TODO: add UseCase (constructor) 'dependency' -> OK

    // TODO: add private and "public" uiState
    private val _uiState = mutableStateOf(ArtworkState())
    val uiState: State<ArtworkState> = _uiState

    // TODO: create "uiState" (data class: Artwork, currentImageDisplayed: string, isAudioEnabled) -> OK

    // TODO: add init (body constructor) which should retrieve artwork's data (call GetArtworkUseCase)
    init {
        savedStateHandle.get<UUID>("beaconId")?.let { artworkId ->
            if(artworkId != null) {
                viewModelScope.launch {
                    artworkUseCase(artworkId, baseURL = "")?.also { artwork ->
                        _uiState.value = uiState.value.copy(artwork = artwork)
                    }
                }
            }
        }
    }
    // TODO: artworkId should be retrieved from SavedStateHandle (see NoteApp)

    // TODO: _uiState.value = uiState.value.copy(artwork = newArtwork) -> OK

    // TODO: add ArtworkEvent (sealed class) (ImageNext, ImagePrev, AudioChange) -> OK

    // TODO: add onEvent method (pattern matching on event type) (see NoteApp)
    fun onEvent(event: ArtworkEvent) {
        when(event){
            is ArtworkEvent.FirstSlide -> {
                _uiState.value = uiState.value.copy(
                    currentImagesNumber = 1
                )
            }
            is ArtworkEvent.ImageNext -> {
                _uiState.value = uiState.value.copy(
                    currentImagesNumber = if(uiState.value.currentImagesNumber < uiState.value.maxImagesNumber) uiState.value.currentImagesNumber + 1 else 1
                )
            }
            is ArtworkEvent.ImagePrevious -> {
                _uiState.value = uiState.value.copy(
                    currentImagesNumber = if(uiState.value.currentImagesNumber > 1) uiState.value.currentImagesNumber - 1 else uiState.value.maxImagesNumber
                )
            }
            is ArtworkEvent.AudioChange -> {
                _uiState.value = uiState.value.copy(
                    isAudioEnabled = !uiState.value.isAudioEnabled
                )
            }
            is ArtworkEvent.LastSlide -> {
                _uiState.value = uiState.value.copy(
                    currentImagesNumber = uiState.value.maxImagesNumber
                )
            }
        }
    }
}