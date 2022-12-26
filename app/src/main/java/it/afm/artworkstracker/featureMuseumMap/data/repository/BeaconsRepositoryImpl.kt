package it.afm.artworkstracker.featureMuseumMap.data.repository

import it.afm.artworkstracker.featureMuseumMap.data.dataSource.beacon.BeaconsDataSource
import it.afm.artworkstracker.featureMuseumMap.domain.model.Beacon
import it.afm.artworkstracker.featureMuseumMap.domain.repository.BeaconsRepository
import kotlinx.coroutines.flow.Flow

class BeaconsRepositoryImpl(
    private val beaconsDataSource: BeaconsDataSource
    ): BeaconsRepository {

    override fun getCloserBeacons(): Flow<List<Beacon>> = beaconsDataSource.getCloserBeacons()

    override fun startListeningForBeacons() {
        beaconsDataSource.startListeningForBeacons()
    }

    override fun stopListeningForBeacons() {
        beaconsDataSource.stopListeningForBeacons()
    }
}