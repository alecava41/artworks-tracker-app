package it.afm.artworkstracker.featureArtwork.presentation

import it.afm.artworkstracker.featureArtwork.domain.model.Artwork
import java.util.*

data class ArtworkState(
    val artwork: Artwork = Artwork(id = UUID.randomUUID(), description = "", author = "", title = ""),
    val isAudioEnabled: Boolean = false,
    val visitedArtworks: List<Artwork> = emptyList()
)