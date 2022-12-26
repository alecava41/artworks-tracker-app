package it.afm.artworkstracker.core.data.dto

import it.afm.artworkstracker.featureMuseumMap.domain.model.Room

data class RoomDto(
    val id: Int,
    val name: String,
    val shape: String,
    val artworks: List<ArtworkInfoDto>) {

    // TODO: handle null cases (room doesn't exist) (room API and artwork API)

    fun toRoom(): Room {
        return Room(
            id = id,
            name = name,
            shape = shape,
            artworks = artworks.map { it.toArtWorkInfo() })
    }
}