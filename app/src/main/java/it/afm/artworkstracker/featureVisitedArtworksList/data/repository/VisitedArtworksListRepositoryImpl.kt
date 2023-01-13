package it.afm.artworkstracker.featureVisitedArtworksList.data.repository

import it.afm.artworkstracker.core.data.local.ArtworkDao
import it.afm.artworkstracker.core.data.local.entity.ArtworkEntity
import it.afm.artworkstracker.featureVisitedArtworksList.domain.repository.VisitedArtworksListRepository
import kotlinx.coroutines.flow.Flow

class VisitedArtworksListRepositoryImpl(
    private val dao: ArtworkDao
) : VisitedArtworksListRepository {
    override fun getVisitedArtworksOrderedByTitle(): Flow<List<ArtworkEntity>> {
        return dao.getVisitedArtworksOrderedByTitle()
    }
}