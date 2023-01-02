package it.afm.artworkstracker.featureMuseumMap.presentation

import it.afm.artworkstracker.featureMuseumMap.domain.model.Beacon
import it.afm.artworkstracker.featureMuseumMap.domain.model.Room

data class MuseumMapState(
    val room: Room = defaultRoom,
    val firstBeaconRanged: Beacon? = null
)
