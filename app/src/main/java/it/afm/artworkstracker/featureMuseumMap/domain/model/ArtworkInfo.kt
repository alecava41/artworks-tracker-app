package it.afm.artworkstracker.featureMuseumMap.domain.model

import it.afm.artworkstracker.featureMuseumMap.domain.util.ArtworkType
import it.afm.artworkstracker.featureMuseumMap.domain.util.Side
import java.util.UUID

data class ArtworkInfo(
    val id: Int,
    val beacon: UUID,
    val posX: Int,
    val posY: Int,
    val starred: Boolean,
    var visited: Boolean,
    val direction: String,
    val side: Side,
    val type: ArtworkType
)