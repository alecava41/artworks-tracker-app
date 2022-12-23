package it.afm.artworkstracker.featureArtwork.domain.useCase

import it.afm.artworkstracker.featureArtwork.domain.model.Artwork
import it.afm.artworkstracker.featureArtwork.domain.repository.ArtworkRepository
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class GetArtworkUseCase(
    private val repository: ArtworkRepository
){
    suspend operator fun invoke(id: UUID): Artwork{
        return repository.getArtworkFromId(id)
    }
}