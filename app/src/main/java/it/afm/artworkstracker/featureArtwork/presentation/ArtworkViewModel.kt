package it.afm.artworkstracker.featureArtwork.presentation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel

//@HiltViewModel
//class ArtworkViewModel : ViewModel() {
    // TODO: add UseCase (constructor) 'dependency'

    // TODO: add private and "public" uiState

    // TODO: create "uiState" (data class: Artwork, currentImageDisplayed: string, isAudioEnabled)

    // TODO: add init (body constructor) which should retrieve artwork's data (call GetArtworkUseCase)
    // TODO: artworkId should be retrieved from SavedStateHandle (see NoteApp)
    // TODO: _uiState.value = uiState.value.copy(artwork = newArtwork)

    // TODO: add ArtworkEvent (sealed class) (ImageNext, ImagePrev)

    // TODO: add onEvent method (pattern matching on event type) (see NoteApp)
//}