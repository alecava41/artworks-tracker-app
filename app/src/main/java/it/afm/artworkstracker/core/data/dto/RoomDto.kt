package it.afm.artworkstracker.core.data.dto

import it.afm.artworkstracker.featureMuseumMap.domain.model.Room
import it.afm.artworkstracker.featureMuseumMap.domain.util.PerimeterEnum

data class RoomDto(
    val id: Int,
    val name: String,
    val shape: String,
    val artworks: List<ArtworkInfoDto>) {

    fun toRoom(): Room {
        val perimeter = arrayListOf<Triple<PerimeterEnum, Int, Int>>()
//        val doors = arrayListOf<Pair<Pair<Int, Int>, Pair<Int, Int>>>()

        val chunks = shape.split('-')

        chunks.forEach {
            when(it[0]) {
                'M' -> { // M(_,_) (MOVE)
                    val x = it.substringBefore(',').substringAfter('(').toInt()
                    val y = it.substringAfter(',').substringBefore(')').toInt()
                    perimeter.add(Triple(PerimeterEnum.MOVE, x, y))
                }
                'L' -> { // L(_,_) (LINE)
                    val x = it.substringBefore(',').substringAfter('(').toInt()
                    val y = it.substringAfter(',').substringBefore(')').toInt()
                    perimeter.add(Triple(PerimeterEnum.LINE, x, y))
                }
//                'D' -> { // D(_,_,_,_) (DOOR)
//                    val values = it.split(',')
//                    val startX = values[0].substringAfter('(').toInt()
//                    val startY = values[1].toInt()
//                    val stopX = values[2].toInt()
//                    val stopY = values[3].substringBefore(')').toInt()
//                    doors.add(Pair(Pair(startX, startY), Pair(stopX, stopY)))
//                }
            }
        }

        return Room(
            id = id,
            name = name,
            perimeter = perimeter,
            artworks = artworks.map { it.toArtWorkInfo() })
    }
}