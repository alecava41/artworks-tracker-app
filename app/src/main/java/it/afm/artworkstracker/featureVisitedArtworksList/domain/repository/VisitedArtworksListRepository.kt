package it.afm.artworkstracker.featureVisitedArtworksList.domain.repository

import it.afm.artworkstracker.core.data.local.entity.ArtworkEntity
import kotlinx.coroutines.flow.Flow

interface VisitedArtworksListRepository {
    fun getVisitedArtworksOrderedByTitle(): Flow<List<ArtworkEntity>>
}