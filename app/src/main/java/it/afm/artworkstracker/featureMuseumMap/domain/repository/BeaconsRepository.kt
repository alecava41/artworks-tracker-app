package it.afm.artworkstracker.featureMuseumMap.domain.repository

import it.afm.artworkstracker.featureMuseumMap.domain.model.Beacon
import kotlinx.coroutines.flow.Flow

interface BeaconsRepository {
    fun getCloserBeacons(): Flow<List<Beacon>>
    fun startListeningForBeacons()
    fun stopListeningForBeacons()
}