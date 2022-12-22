package it.afm.artworkstracker.featureMuseumMap.domain.model

import it.afm.artworkstracker.featureArtwork.domain.model.ArtworkInfo

data class Room(
    val id: Int,
    val name: String,
    val shape: String,
    val artworks: List<ArtworkInfo>
)