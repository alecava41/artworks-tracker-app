package it.afm.artworkstracker.featureMuseumMap.domain.model

import java.util.UUID

data class ArtworkBeacon(
    val id: UUID,
    val direction: String
)
