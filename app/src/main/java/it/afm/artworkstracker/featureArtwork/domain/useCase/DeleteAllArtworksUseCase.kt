package it.afm.artworkstracker.featureArtwork.domain.useCase

import it.afm.artworkstracker.featureArtwork.domain.repository.ArtworkRepository

class DeleteAllArtworksUseCase(
    private val repository: ArtworkRepository
) {
    suspend operator fun invoke() {
        repository.deleteAllArtworks()
    }
}