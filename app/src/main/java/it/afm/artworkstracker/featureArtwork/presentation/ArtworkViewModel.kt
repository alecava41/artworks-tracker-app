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

//@HiltViewModel
class ArtworkViewModel(
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
        savedStateHandle.get<UUID>("noteId")?.let { artworkId ->
            if(artworkId != null) {
                viewModelScope.launch {
                    artworkUseCase(artworkId)?.also { artwork ->
                        _uiState.value = uiState.value.copy(artwork = artwork)
                    }
                }
            }
        }
    }
    // TODO: artworkId should be retrieved from SavedStateHandle (see NoteApp)

    // TODO: _uiState.value = uiState.value.copy(artwork = newArtwork) -> OK

    // TODO: add ArtworkEvent (sealed class) (ImageNext, ImagePrev)

    // TODO: add onEvent method (pattern matching on event type) (see NoteApp)
    fun onEvent(event: ArtworkEvent) {
        when(event){
            is ArtworkEvent.ImageNext -> {
                _uiState.value = uiState.value.copy(
                    artwork = uiState.value.artwork,
                    currentImageDisplayed = uiState.value.currentImageDisplayed,
                    isAudioEnabled = uiState.value.isAudioEnabled,
                    currentImagesNumber = if(uiState.value.currentImagesNumber < uiState.value.maxImagesNumber) uiState.value.currentImagesNumber + 1 else 1,
                    maxImagesNumber = uiState.value.maxImagesNumber
                )
            }
            is ArtworkEvent.ImagePrevious -> {
                _uiState.value = uiState.value.copy(
                    artwork = uiState.value.artwork,
                    currentImageDisplayed = uiState.value.currentImageDisplayed,
                    isAudioEnabled = uiState.value.isAudioEnabled,
                    currentImagesNumber = if(uiState.value.currentImagesNumber > 1) uiState.value.currentImagesNumber - 1 else uiState.value.maxImagesNumber,
                    maxImagesNumber = uiState.value.maxImagesNumber
                )
            }
            is ArtworkEvent.AudioChange -> {
                _uiState.value = uiState.value.copy(
                    artwork = uiState.value.artwork,
                    currentImageDisplayed = uiState.value.currentImageDisplayed,
                    isAudioEnabled = !uiState.value.isAudioEnabled,
                    currentImagesNumber = uiState.value.currentImagesNumber,
                    maxImagesNumber = uiState.value.maxImagesNumber
                )
            }
        }
    }
}