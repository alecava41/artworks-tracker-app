package it.afm.artworkstracker.core.data.dto

import it.afm.artworkstracker.featureArtwork.data.dataSource.local.entity.ArtworkEntity
import java.util.UUID

data class ArtworkDto(
    val author: String,
    val title: String,
    val description: String,
    val id: UUID
) {
    fun toArtworkEntity(): ArtworkEntity {
        return ArtworkEntity(
            author = author,
            title = title,
            description = description,
            id = id
        )
    }
}