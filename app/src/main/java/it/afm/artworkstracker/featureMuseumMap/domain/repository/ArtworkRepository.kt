package it.afm.artworkstracker.featureMuseumMap.domain.repository

import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface ArtworkRepository {
    fun getArtworksId(): Flow<List<UUID>>
}