package it.afm.artworkstracker.featureArtwork.domain.useCase

import it.afm.artworkstracker.featureArtwork.domain.model.Artwork
import it.afm.artworkstracker.featureArtwork.domain.repository.ArtworkRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetVisitedArtworksUseCase(
    private val repository: ArtworkRepository
) {
    suspend operator fun invoke(): Flow<List<Artwork>> {
        return repository.getVisitedArtworksOrderedByTitle()
            .map { list -> list.map { artworkEntity -> artworkEntity.toArtwork() } }
    }
}