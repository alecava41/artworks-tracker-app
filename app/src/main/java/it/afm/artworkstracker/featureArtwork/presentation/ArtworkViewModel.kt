package it.afm.artworkstracker.featureArtwork.presentation

//import android.util.Log
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
    private val _uiState = mutableStateOf(ArtworkState())
    val uiState: State<ArtworkState> = _uiState

    init {
        savedStateHandle.get<String>("artId")?.let { artworkId ->
            savedStateHandle.get<String>("url")?.let { url ->
                viewModelScope.launch {
                    artworkUseCase(id = UUID.fromString(artworkId), baseURL = url)?.also { artwork ->
                        _uiState.value = uiState.value.copy(artwork = artwork)
                    }
            }
            }
        }
    }


    fun onEvent(event: ArtworkEvent) {
        when(event){
/*            is ArtworkEvent.FirstSlide -> {
                _uiState.value = uiState.value.copy(
                    currentImagesNumber = 0
                )
                //Log.i("Current Image Number Public", uiState.value.currentImagesNumber.toString())
                //Log.i("Current Image Number Private", _uiState.value.currentImagesNumber.toString())
            }
            is ArtworkEvent.ImageNext -> {
                _uiState.value = uiState.value.copy(
                    currentImagesNumber = if(uiState.value.currentImagesNumber < uiState.value.maxImagesNumber) uiState.value.currentImagesNumber + 1 else 0
                )
                //Log.i("Current Image Number Public", uiState.value.currentImagesNumber.toString())
                //Log.i("Current Image Number Private", _uiState.value.currentImagesNumber.toString())
            }
            is ArtworkEvent.ImagePrevious -> {
                _uiState.value = uiState.value.copy(
                    currentImagesNumber = if(uiState.value.currentImagesNumber > 0) uiState.value.currentImagesNumber - 1 else uiState.value.maxImagesNumber
                )
                //Log.i("Current Image Number Public", uiState.value.currentImagesNumber.toString())
                //Log.i("Current Image Number Private", _uiState.value.currentImagesNumber.toString())
            }*/
            is ArtworkEvent.AudioChange -> {
                _uiState.value = uiState.value.copy(
                    isAudioEnabled = !uiState.value.isAudioEnabled
                )
                //Log.i("Current Image Number Public", uiState.value.currentImagesNumber.toString())
                //Log.i("Current Image Number Private", _uiState.value.currentImagesNumber.toString())
            }
/*            is ArtworkEvent.LastSlide -> {
                _uiState.value = uiState.value.copy(
                    currentImagesNumber = uiState.value.maxImagesNumber
                )
                //Log.i("Current Image Number Public", uiState.value.currentImagesNumber.toString())
                //Log.i("Current Image Number Private", _uiState.value.currentImagesNumber.toString())
            }*/
        }
    }
}