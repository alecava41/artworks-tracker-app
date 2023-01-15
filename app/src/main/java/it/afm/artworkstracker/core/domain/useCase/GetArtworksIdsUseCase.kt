package it.afm.artworkstracker.core.domain.useCase

import it.afm.artworkstracker.featureMuseumMap.domain.repository.ArtworkRepository
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class GetArtworksIdsUseCase(
    private val repository: ArtworkRepository
) {
    operator fun invoke(): Flow<List<UUID>> {
        return repository.getArtworksId()
    }
}