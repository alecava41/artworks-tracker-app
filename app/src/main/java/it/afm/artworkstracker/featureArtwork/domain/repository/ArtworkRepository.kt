package it.afm.artworkstracker.featureArtwork.domain.repository

import it.afm.artworkstracker.featureArtwork.domain.model.Artwork
import java.util.UUID

interface ArtworkRepository {
    suspend fun getArtworkFromId(id: UUID): Artwork // do we put (Artwork?) ?
    suspend fun deleteAllArtworks()
    suspend fun insertArtwork(artwork: Artwork)
}