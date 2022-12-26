package it.afm.artworkstracker.featureMuseumMap.data.repository


import it.afm.artworkstracker.core.data.MuseumApi
import it.afm.artworkstracker.featureMuseumMap.domain.model.Room
import it.afm.artworkstracker.featureMuseumMap.domain.repository.RoomRepository

class RoomRepositoryImpl(
    private val api: MuseumApi
): RoomRepository {
    override suspend fun getRoom(url: String): Room? {
        return api.getRoom(url)?.toRoom()
    }
}