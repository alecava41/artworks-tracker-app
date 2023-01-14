package it.afm.artworkstracker.featureArtwork.presentation

import it.afm.artworkstracker.core.domain.model.Artwork
import java.util.*

data class ArtworkState(
    val artwork: Artwork? = null,
    val isAudioEnabled: Boolean = false
)