package it.afm.artworkstracker.featureMuseumMap.domain.model

import java.util.UUID

data class ArtworkInfo(
    val id: Int,
    val beacon: UUID,
    val posX: String,
    val posY: String,
    val starred: Boolean,
    val type: String
    )