package it.afm.artworkstracker.featureMuseumMap.data.repository

import it.afm.artworkstracker.core.data.local.ArtworkDao
import it.afm.artworkstracker.featureMuseumMap.domain.repository.ArtworkRepository
import kotlinx.coroutines.flow.Flow
import java.util.*

class ArtworkRepositoryImpl(
    private val dao: ArtworkDao
): ArtworkRepository {
    override fun getArtworksId(): Flow<List<UUID>> {
       return dao.getArtworksId()
    }
}