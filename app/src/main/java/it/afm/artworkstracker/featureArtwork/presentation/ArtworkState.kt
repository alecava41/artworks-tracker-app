package it.afm.artworkstracker.featureArtwork.presentation

import it.afm.artworkstracker.featureArtwork.domain.model.Artwork

data class ArtworkState(
    val artwork: Artwork? = null,
    val currentImageDisplayed: String? = null,
    val isAudioEnabled: Boolean = false,
    val currentImagesNumber: Int = 1,
    val maxImagesNumber: Int = 3
)