package it.afm.artworkstracker.core.data.remote.dto

import it.afm.artworkstracker.featureMuseumMap.domain.model.Room
import it.afm.artworkstracker.featureMuseumMap.domain.util.PerimeterEntity

data class RoomDto(
    val id: Int,
    val name: String,
    val shape: String,
    val artworks: List<ArtworkInfoDto>
) {

    fun toRoom(): Room {
        val perimeter = arrayListOf<Triple<PerimeterEntity, Int, Int>>()

        val chunks = shape.split('-')

        chunks.forEach {
            when (it[1]) {
                'M' -> { // (M,_,_) (MOVE)
                    val x = it.substringAfter(',')
                        .substringBefore(',')
                        .toInt()
                    val y = it.substringAfter(',')
                        .substringAfter(',')
                        .substringBefore(')').toInt()
                    perimeter.add(Triple(PerimeterEntity.MOVE, x, y))
                }
                'L' -> { // (L,_,_) (LINE)
                    val x = it.substringAfter(',')
                        .substringBefore(',')
                        .toInt()
                    val y = it.substringAfter(',')
                        .substringAfter(',')
                        .substringBefore(')').toInt()
                    perimeter.add(Triple(PerimeterEntity.LINE, x, y))
                }
            }
        }

        return Room(
            id = id,
            name = name,
            perimeter = perimeter,
            artworks = artworks.map { it.toArtWorkInfo() })
    }
}