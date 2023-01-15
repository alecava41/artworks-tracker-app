package it.afm.artworkstracker.featureArtwork.domain.repository

import it.afm.artworkstracker.core.domain.model.Artwork
import java.util.*

interface ArtworkRepository {
    suspend fun getArtworkFromId(id:UUID, url: String): Artwork?
}