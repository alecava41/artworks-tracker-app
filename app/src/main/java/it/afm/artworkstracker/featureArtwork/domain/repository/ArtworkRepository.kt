package it.afm.artworkstracker.featureArtwork.domain.repository

import it.afm.artworkstracker.featureArtwork.data.dataSource.local.entity.ArtworkEntity
import it.afm.artworkstracker.featureArtwork.domain.model.Artwork
import java.util.UUID

interface ArtworkRepository {
    suspend fun getArtworkFromId(id: UUID): Artwork?
    suspend fun deleteAllArtworks()
    suspend fun insertArtwork(artworkEntity: ArtworkEntity)
}