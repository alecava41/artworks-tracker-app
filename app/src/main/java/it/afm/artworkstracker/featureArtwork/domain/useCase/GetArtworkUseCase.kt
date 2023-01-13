package it.afm.artworkstracker.featureArtwork.domain.useCase

import it.afm.artworkstracker.core.domain.model.Artwork
import it.afm.artworkstracker.featureArtwork.domain.repository.ArtworkRepository
import java.util.*

class GetArtworkUseCase(
    private val repository: ArtworkRepository
){
    suspend operator fun invoke(id: UUID, baseURL: String, language: String): Artwork? {
        return repository.getArtworkFromId(id, "$baseURL/api/artworks/$id?lan=$language")
    }
}