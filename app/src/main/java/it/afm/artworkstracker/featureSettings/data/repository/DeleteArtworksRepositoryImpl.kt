package it.afm.artworkstracker.featureSettings.data.repository

import it.afm.artworkstracker.core.data.local.ArtworkDao
import it.afm.artworkstracker.featureSettings.domain.repository.DeleteArtworksRepository

class DeleteArtworksRepositoryImpl(
    private val dao: ArtworkDao
): DeleteArtworksRepository {
    override suspend fun deleteAllArtworks() {
        dao.deleteAllArtworks()
    }
}