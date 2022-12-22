package it.afm.artworkstracker.core

import it.afm.artworkstracker.core.dto.ArtworkDto
import it.afm.artworkstracker.core.dto.RoomDto

interface MuseumApi {
    fun getRoom(relativePath: String): RoomDto
    fun getArtwork(relativePath: String): ArtworkDto
}