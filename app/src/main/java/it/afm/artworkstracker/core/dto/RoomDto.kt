package it.afm.artworkstracker.core.dto

import it.afm.artworkstracker.featureMuseumMap.domain.model.Room

data class RoomDto(
    val id: Int,
    val name: String,
    val shape: String,
    val artworks: List<ArtworkInfoDto>) {
    fun toRoom(): Room {
        return Room(
            id = id,
            name = name,
            shape = shape,
            artworks = artworks.map { it.toArtWorkInfo() })
    }
}