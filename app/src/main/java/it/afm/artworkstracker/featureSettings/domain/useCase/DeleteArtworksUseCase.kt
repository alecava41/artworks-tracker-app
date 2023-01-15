package it.afm.artworkstracker.featureSettings.domain.useCase

import it.afm.artworkstracker.featureSettings.domain.repository.DeleteArtworksRepository

class DeleteArtworksUseCase(
    private val repository: DeleteArtworksRepository
) {
    suspend operator fun invoke() {
        repository.deleteAllArtworks()
    }
}