package it.afm.artworkstracker.core.data.remote.dto

import it.afm.artworkstracker.featureMuseumMap.domain.model.Room
import it.afm.artworkstracker.featureMuseumMap.domain.util.PerimeterEntity

data class RoomDto(
    val id: Int,
    val name: String,
    val perimeter: String,
    val walls: String,
    val starredPath: String,
    val artworks: List<ArtworkInfoDto>
) {

    fun toRoom(): Room {
        return Room(
            id = id,
            name = name,
            perimeter = extractRoomData(perimeter),
            walls = extractRoomData(walls),
            starredPath = extractRoomData(starredPath),
            artworks = artworks.map { it.toArtWorkInfo() })
    }

    private fun extractRoomData(source: String): ArrayList<Triple<PerimeterEntity, Int, Int>> {
        val entities = arrayListOf<Triple<PerimeterEntity, Int, Int>>()

        if (source.isNotEmpty() && source.isNotBlank()) {
            val chunks = source.split('-')

            chunks.forEach {
                when (it[1]) {
                    'M' -> { // (M,_,_) (MOVE)
                        val x = it.substringAfter(',')
                            .substringBefore(',')
                            .toInt()
                        val y = it.substringAfter(',')
                            .substringAfter(',')
                            .substringBefore(')').toInt()
                        entities.add(Triple(PerimeterEntity.MOVE, x, y))
                    }
                    'L' -> { // (L,_,_) (LINE)
                        val x = it.substringAfter(',')
                            .substringBefore(',')
                            .toInt()
                        val y = it.substringAfter(',')
                            .substringAfter(',')
                            .substringBefore(')').toInt()
                        entities.add(Triple(PerimeterEntity.LINE, x, y))
                    }
                }
            }
        }

        return entities
    }
}