package it.afm.artworkstracker.featureMuseumMap.data.dataSource

import it.afm.artworkstracker.featureMuseumMap.domain.model.Beacon
import kotlinx.coroutines.flow.Flow

interface BeaconsDataSource {
    fun getCloserBeacons(): Flow<List<Beacon>>
    fun startListeningForBeacons()
    fun stopListeningForBeacons()
}