package it.afm.artworkstracker.featureArtwork.data.repository

import it.afm.artworkstracker.core.MuseumApi
import it.afm.artworkstracker.featureArtwork.data.dataSource.local.ArtworkDao
import it.afm.artworkstracker.featureArtwork.domain.model.Artwork
import it.afm.artworkstracker.featureArtwork.domain.repository.ArtworkRepository
import java.io.IOException
import java.util.UUID

class ArtworkRepositoryImpl(
    private val api: MuseumApi,
    private val dao: ArtworkDao
) : ArtworkRepository {
    override suspend fun getArtwork(id: UUID): Artwork {
        lateinit var nearestArtwork: Artwork
        val searchArtwork = dao.getArtworkFromId(id)
        if(searchArtwork == null){
            try {
                nearestArtwork = api.getArtwork(relativePath = "").toArtwork()
                dao.insertArtwork(nearestArtwork)
            }//catch(e: HttpException){ // retrofit type

            //}
        catch (e: IOException){

            }
        } else {
            nearestArtwork = searchArtwork.toArtwork()
        }
        return nearestArtwork
    }
}