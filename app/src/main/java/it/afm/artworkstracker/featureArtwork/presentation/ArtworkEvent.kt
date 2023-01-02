package it.afm.artworkstracker.featureArtwork.presentation

sealed class ArtworkEvent{
    object AudioChange: ArtworkEvent()
    data class SpeechStatus(val isSpeaking: Boolean): ArtworkEvent()
}