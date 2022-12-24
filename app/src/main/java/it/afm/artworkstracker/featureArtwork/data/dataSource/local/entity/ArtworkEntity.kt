package it.afm.artworkstracker.featureArtwork.data.dataSource.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import it.afm.artworkstracker.featureArtwork.domain.model.Artwork
import java.util.UUID

@Entity
data class ArtworkEntity(
    val author: String,
    val title: String,
    val description: String,
    @PrimaryKey val id: UUID
){
    fun toArtwork(): Artwork{
        return Artwork(
            author = author,
            title = title,
            description = description,
            id = id
        )
    }
}