package it.afm.artworkstracker.featureMuseumMap.presentation

import it.afm.artworkstracker.featureMuseumMap.domain.model.ArtworkBeacon
import it.afm.artworkstracker.featureMuseumMap.domain.model.Room

data class MuseumMapState(
    val isAudioEnabled: Boolean = false,
    val room: Room? = null,
    val currentArtwork: ArtworkBeacon? = null,
    val lastArtwork: ArtworkBeacon? = null
)
