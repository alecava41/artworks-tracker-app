package it.afm.artworkstracker.featureArtwork.presentation

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.afm.artworkstracker.featureArtwork.domain.useCase.GetArtworkUseCase
import it.afm.artworkstracker.util.LanguageUtil
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ArtworkViewModel @Inject constructor(
    private val getArtworkUseCase: GetArtworkUseCase,
    app: Application,
    savedStateHandle: SavedStateHandle
) : AndroidViewModel(
    app
) {
    private val _uiState = mutableStateOf(ArtworkState())
    val uiState: State<ArtworkState> = _uiState

    lateinit var url: String
        private set

    private val lan = String().let {
        val firstLan = getApplication<Application>().applicationContext.resources.configuration.locales[0]

        if (LanguageUtil.supportedLanguages.contains(firstLan.language)) firstLan.language
        else Locale.ENGLISH.language
    }

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
                        _uiState.value = uiState.value.copy(artwork = artwork)
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