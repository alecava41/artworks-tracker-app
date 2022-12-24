package it.afm.artworkstracker.core

import it.afm.artworkstracker.core.dto.ArtworkDto
import it.afm.artworkstracker.core.dto.RoomDto

interface MuseumApi {
    // TODO: add missing Retrofit annotations
    suspend fun getRoom(relativePath: String): RoomDto?

    // TODO: add missing Retrofit annotations
    suspend fun getArtwork(relativePath: String): ArtworkDto?
}