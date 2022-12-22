package it.afm.artworkstracker.featureArtwork.domain.repository

import it.afm.artworkstracker.featureArtwork.domain.model.Artwork
import java.util.UUID

interface ArtworkRepository {
    suspend fun getArtwork(id: UUID): Artwork
}