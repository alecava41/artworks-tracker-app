package it.afm.artworkstracker.featureArtwork.presentation

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.afm.artworkstracker.featureArtwork.domain.useCase.GetArtworkUseCase
import it.afm.artworkstracker.featureArtwork.domain.useCase.GetVisitedArtworksUseCase
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ArtworkViewModel @Inject constructor(
    private val getArtworkUseCase: GetArtworkUseCase,
    private val getVisitedArtworksUseCase: GetVisitedArtworksUseCase,
    app: Application,
    savedStateHandle: SavedStateHandle
) : AndroidViewModel(
    app
) {
    private val _uiState = mutableStateOf(ArtworkState())
    val uiState: State<ArtworkState> = _uiState

    lateinit var url: String
        private set

    private val lan: String = getApplication<Application>().applicationContext.resources.configuration.locales[0].language

    init {
        savedStateHandle.get<String>("artId")?.let { artworkId ->
            savedStateHandle.get<String>("url")?.let { url ->
                this.url = url
                viewModelScope.launch {
                    getArtworkUseCase(
                        id = UUID.fromString(artworkId),
                        baseURL = url,
                        language = lan
                    )?.also { artwork ->
                        // TODO: handle if artwork is not found
                        _uiState.value = uiState.value.copy(artwork = artwork)
                    }
                    getVisitedArtworksUseCase().onEach { visitedArtworks ->
                        _uiState.value = uiState.value.copy(visitedArtworks = visitedArtworks)
                    }
                }
            }
        }
    }

    fun onEvent(event: ArtworkEvent) {
        when (event) {
            is ArtworkEvent.SpeechStatus -> {
                _uiState.value = uiState.value.copy(
                    isAudioEnabled = event.isSpeaking
                )
            }
        }
    }
}