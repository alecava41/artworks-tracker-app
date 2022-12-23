package it.afm.artworkstracker.featureArtwork.domain.useCase

import it.afm.artworkstracker.featureArtwork.domain.model.Artwork
import it.afm.artworkstracker.featureArtwork.domain.repository.ArtworkRepository

class InsertArtworkUseCase(
    private val repository: ArtworkRepository
) {
    suspend operator fun invoke(artwork: Artwork) {
        repository.insertArtwork(artwork)
    }
}