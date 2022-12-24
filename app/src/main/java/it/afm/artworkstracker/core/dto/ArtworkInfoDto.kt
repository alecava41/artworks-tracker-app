package it.afm.artworkstracker.core.dto

import it.afm.artworkstracker.featureMuseumMap.domain.model.ArtworkInfo
import java.util.UUID

data class ArtworkInfoDto(
    val id: UUID,
    val posX: String,
    val posY: String,
    val starred: Boolean,
    val type: String
    ) {
    fun toArtWorkInfo(): ArtworkInfo {
        return ArtworkInfo(
            id = id,
            posX = posX,
            posY = posY,
            starred = starred,
            type = type
        )
    }
}