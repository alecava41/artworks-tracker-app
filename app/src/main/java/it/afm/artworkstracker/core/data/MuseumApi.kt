package it.afm.artworkstracker.core.data

import it.afm.artworkstracker.core.data.dto.ArtworkDto
import it.afm.artworkstracker.core.data.dto.RoomDto
import retrofit2.http.GET
import retrofit2.http.Url

interface MuseumApi {
    @GET
    suspend fun getRoom(@Url path: String): RoomDto?

    @GET
    suspend fun getArtwork(@Url path: String): ArtworkDto?
}