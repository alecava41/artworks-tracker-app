package it.afm.artworkstracker.core.data.dto

import it.afm.artworkstracker.featureMuseumMap.domain.model.ArtworkInfo
import it.afm.artworkstracker.featureMuseumMap.domain.util.ArtworkEnum
import java.util.UUID

data class ArtworkInfoDto(
    val id: Int,
    val beacon: UUID,
    val posX: Int,
    val posY: Int,
    val starred: Boolean,
    val type: String
) {
    fun toArtWorkInfo(): ArtworkInfo {
        val type = when (type) {
            "Painting" -> ArtworkEnum.PICTURE
            "Sculpture" -> ArtworkEnum.SCULPTURE
            else -> ArtworkEnum.PICTURE // TODO: decide what to do
        }
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