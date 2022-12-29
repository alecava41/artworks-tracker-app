package it.afm.artworkstracker.featureMuseumMap.presentation

import it.afm.artworkstracker.featureMuseumMap.domain.model.Beacon
import it.afm.artworkstracker.featureMuseumMap.domain.model.Room

data class MuseumMapState(
    val closestBeacon: Beacon? = null, // TODO: to be removed (maybe)
    val room: Room? = null
)
