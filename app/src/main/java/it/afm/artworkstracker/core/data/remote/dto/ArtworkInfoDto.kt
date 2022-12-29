package it.afm.artworkstracker.core.data.remote.dto

import it.afm.artworkstracker.featureMuseumMap.domain.model.ArtworkInfo
import it.afm.artworkstracker.featureMuseumMap.domain.util.ArtworkType
import it.afm.artworkstracker.featureMuseumMap.domain.util.Side
import java.util.UUID

data class ArtworkInfoDto(
    val id: Int,
    val beacon: UUID,
    val posX: Int,
    val posY: Int,
    val starred: Boolean,
    val side: Char,
    val type: String
) {
    fun toArtWorkInfo(): ArtworkInfo {
        val type = when (type) {
            "Painting" -> ArtworkType.PICTURE
            "Sculpture" -> ArtworkType.SCULPTURE
            else -> ArtworkType.PICTURE // TODO: decide what to do
        }

        val side = when(side) {
            'L' -> Side.LEFT
            'R' -> Side.RIGHT
            'F' -> Side.UP
            'B' -> Side.DOWN
            else -> Side.DOWN
        }

        return ArtworkInfo(
            id = id,
            beacon = beacon,
            posX = posX,
            posY = posY,
            starred = starred,
            visited = false, // TODO: add logic to determine this state,
            side = side,
            type = type
        )
    }
}