package it.afm.artworkstracker.featureArtwork.presentation

sealed class ArtworkEvent{
    data class SpeechStatus(val isSpeaking: Boolean): ArtworkEvent()
}