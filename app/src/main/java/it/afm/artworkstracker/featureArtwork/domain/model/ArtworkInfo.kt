package it.afm.artworkstracker.featureArtwork.domain.model

import java.util.UUID

data class ArtworkInfo(
    val id: UUID,
    val posX: String,
    val posY: String,
    val starred: Boolean,
    val type: String
    )