package it.afm.artworkstracker.core.domain.model

import java.util.UUID

data class Artwork(
    val author: String,
    val title: String,
    val description: String,
    val id: UUID
)