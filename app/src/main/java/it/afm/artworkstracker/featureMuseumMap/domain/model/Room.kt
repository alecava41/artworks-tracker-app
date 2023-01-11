package it.afm.artworkstracker.featureMuseumMap.domain.model

import it.afm.artworkstracker.featureMuseumMap.domain.util.PerimeterEntity

data class Room(
    val id: Int,
    val name: String,
    val perimeter: List<Triple<PerimeterEntity, Int, Int>>,
    val walls: List<Triple<PerimeterEntity, Int, Int>>,
    val starredPath: List<Triple<PerimeterEntity, Int, Int>>,
    val artworks: List<ArtworkInfo>
)