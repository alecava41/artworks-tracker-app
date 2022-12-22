package it.afm.artworkstracker.core.dto

import it.afm.artworkstracker.featureArtwork.domain.model.Artwork
import java.util.UUID

data class ArtworkDto(
    val author: String,
    val title: String,
    val description: String,
    val id: UUID
) {
    fun toArtwork(): Artwork {
        return Artwork(
            author = author,
            title = title,
            description = description,
            id = id
        )
    }
}