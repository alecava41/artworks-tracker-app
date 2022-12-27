package it.afm.artworkstracker.featureArtwork.data.repository

import android.util.Log
import it.afm.artworkstracker.core.data.MuseumApi
import it.afm.artworkstracker.featureArtwork.data.dataSource.local.ArtworkDao
import it.afm.artworkstracker.featureArtwork.data.dataSource.local.entity.ArtworkEntity
import it.afm.artworkstracker.featureArtwork.domain.model.Artwork
import it.afm.artworkstracker.featureArtwork.domain.repository.ArtworkRepository
import retrofit2.HttpException
import java.io.IOException
import java.util.UUID

class ArtworkRepositoryImpl(
    private val api: MuseumApi,
    private val dao: ArtworkDao
) : ArtworkRepository {

    override suspend fun getArtworkFromId(id:UUID, url: String): Artwork? {
        var searchArtwork = dao.getArtworkFromId(id)
        if (searchArtwork == null) {
            try {
                searchArtwork = api.getArtwork(url)?.toArtworkEntity()

                if (searchArtwork != null)
                    dao.insertArtwork(searchArtwork)

            } catch(e: HttpException){
                Log.e(TAG, e.message())
            }
            catch (e: IOException) {
                Log.e(TAG, e.message ?: "IOException")
            }
        }
        return searchArtwork?.toArtwork()
    }

    override suspend fun deleteAllArtworks() {
        dao.deleteAllArtworks()
    }

    override suspend fun insertArtwork(artworkEntity: ArtworkEntity) {
        dao.insertArtwork(artworkEntity)
    }

    companion object {
        const val TAG = "ArtworkRepositoryImpl"
    }
}