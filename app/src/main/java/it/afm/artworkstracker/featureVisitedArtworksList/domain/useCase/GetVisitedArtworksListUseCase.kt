package it.afm.artworkstracker.featureVisitedArtworksList.domain.useCase

import it.afm.artworkstracker.core.domain.model.Artwork
import it.afm.artworkstracker.featureVisitedArtworksList.domain.repository.VisitedArtworksListRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetVisitedArtworksListUseCase(
    private val repository: VisitedArtworksListRepository
) {
    operator fun invoke(): Flow<List<Artwork>> {
        return repository.getVisitedArtworksOrderedByTitle().map { list ->
            list.map { artworkEntity -> artworkEntity.toArtwork() } }
    }
}