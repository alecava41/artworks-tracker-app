package it.afm.artworkstracker.core.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import it.afm.artworkstracker.core.data.local.entity.ArtworkEntity
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface ArtworkDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArtwork(artworkEntity: ArtworkEntity)

    @Query("DELETE FROM ArtworkEntity")
    suspend fun deleteAllArtworks()

    @Query("SELECT * FROM ArtworkEntity WHERE id = :id")
    suspend fun getArtworkFromId(id: UUID): ArtworkEntity?

    @Query("SELECT id FROM ArtworkEntity")
    fun getArtworksId(): Flow<List<UUID>>

    @Query("SELECT * FROM ArtworkEntity ORDER BY ArtworkEntity.title")
    fun getVisitedArtworksOrderedByTitle(): Flow<List<ArtworkEntity>>

}