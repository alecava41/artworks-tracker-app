package it.afm.artworkstracker.featureArtwork.data.dataSource.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import it.afm.artworkstracker.featureArtwork.data.dataSource.local.entity.ArtworkEntity
import it.afm.artworkstracker.featureArtwork.domain.model.Artwork
import java.util.UUID

@Dao
interface ArtworkDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArtwork(artworkEntity: ArtworkEntity)

    @Query("DELETE FROM ArtworkEntity")
    suspend fun deleteAllArtworks()

    @Query("SELECT * FROM ArtworkEntity WHERE id = :id")
    suspend fun getArtworkFromId(id: UUID): ArtworkEntity?

}