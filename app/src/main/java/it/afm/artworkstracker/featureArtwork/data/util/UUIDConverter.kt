package it.afm.artworkstracker.featureArtwork.data.util

import androidx.room.TypeConverter
import java.util.UUID

class UUIDConverter {
    @TypeConverter
    fun fromUUIDToString(uuid: UUID): String{
        return uuid.toString()
    }

    @TypeConverter
    fun fromStringToUUID(str: String): UUID{
        return UUID.fromString(str)
    }
}