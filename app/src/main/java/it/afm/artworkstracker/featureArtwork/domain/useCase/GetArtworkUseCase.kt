package it.afm.artworkstracker.featureArtwork.domain.useCase

import it.afm.artworkstracker.featureArtwork.domain.model.Artwork
import it.afm.artworkstracker.featureArtwork.domain.repository.ArtworkRepository
import kotlinx.coroutines.flow.Flow

class GetArtworkUseCase(
    private val repository: ArtworkRepository
){
    operator fun invoke(): Flow<Artwork>{
        TODO("Not yet implemented")
    }
}