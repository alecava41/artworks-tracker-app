package it.afm.artworkstracker.featureArtwork.domain.repository

import it.afm.artworkstracker.core.data.local.entity.ArtworkEntity
import it.afm.artworkstracker.featureArtwork.domain.model.Artwork
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface ArtworkRepository {
    suspend fun getArtworkFromId(id:UUID, url: String): Artwork?
    suspend fun deleteAllArtworks()
    suspend fun insertArtwork(artworkEntity: ArtworkEntity)
    suspend fun getVisitedArtworksOrderedByTitle(): Flow<List<ArtworkEntity>>
}