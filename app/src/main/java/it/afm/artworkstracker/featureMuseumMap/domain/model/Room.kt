package it.afm.artworkstracker.featureMuseumMap.domain.model

data class Room(
    val id: Int,
    val name: String,
    val shape: String,
    val artworks: List<ArtworkInfo>
)