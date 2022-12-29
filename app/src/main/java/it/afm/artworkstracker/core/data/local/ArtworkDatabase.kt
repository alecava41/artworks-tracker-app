package it.afm.artworkstracker.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import it.afm.artworkstracker.core.data.local.entity.ArtworkEntity
import it.afm.artworkstracker.featureArtwork.data.util.UUIDConverter

@Database(
    entities = [ArtworkEntity::class],
    version = 1
)

@TypeConverters(UUIDConverter::class)
abstract class ArtworkDatabase : RoomDatabase() {
    abstract val dao: ArtworkDao

    companion object{
        const val DB_NAME = "ArtworkDatabase"
    }
}