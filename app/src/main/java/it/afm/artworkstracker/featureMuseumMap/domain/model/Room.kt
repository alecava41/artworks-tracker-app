package it.afm.artworkstracker.featureMuseumMap.domain.model

import it.afm.artworkstracker.featureMuseumMap.domain.util.PerimeterEnum

data class Room(
    val id: Int,
    val name: String,
    val perimeter: List<Triple<PerimeterEnum, Int, Int>>,
    val artworks: List<ArtworkInfo>
)