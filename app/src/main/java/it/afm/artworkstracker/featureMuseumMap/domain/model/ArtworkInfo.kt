package it.afm.artworkstracker.featureMuseumMap.domain.model

import it.afm.artworkstracker.featureMuseumMap.domain.util.ArtworkEnum
import java.util.UUID

data class ArtworkInfo(
    val id: Int,
    val beacon: UUID,
    val posX: Int,
    val posY: Int,
    val starred: Boolean,
    val type: ArtworkEnum
)