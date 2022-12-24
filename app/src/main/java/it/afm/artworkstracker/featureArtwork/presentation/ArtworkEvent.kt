package it.afm.artworkstracker.featureArtwork.presentation

sealed class ArtworkEvent{
    object ImageNext: ArtworkEvent()
    object ImagePrevious: ArtworkEvent()
}