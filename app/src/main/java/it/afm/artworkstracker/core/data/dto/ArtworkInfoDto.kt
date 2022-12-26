package it.afm.artworkstracker.core.data.dto

import it.afm.artworkstracker.featureMuseumMap.domain.model.ArtworkInfo
import java.util.UUID

data class ArtworkInfoDto(
    val id: Int,
    val beacon: UUID,
    val posX: String,
    val posY: String,
    val starred: Boolean,
    val type: String
    ) {
    fun toArtWorkInfo(): ArtworkInfo {
        return ArtworkInfo(
            id = id,
            beacon = beacon,
            posX = posX,
            posY = posY,
            starred = starred,
            type = type
        )
    }
}