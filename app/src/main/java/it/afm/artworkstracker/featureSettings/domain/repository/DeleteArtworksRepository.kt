package it.afm.artworkstracker.featureSettings.domain.repository

interface DeleteArtworksRepository {
    suspend fun deleteAllArtworks()
}