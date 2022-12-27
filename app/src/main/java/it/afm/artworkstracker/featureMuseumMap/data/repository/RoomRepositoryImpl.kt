package it.afm.artworkstracker.featureMuseumMap.data.repository


import android.util.Log
import it.afm.artworkstracker.core.data.MuseumApi
import it.afm.artworkstracker.featureMuseumMap.domain.model.Room
import it.afm.artworkstracker.featureMuseumMap.domain.repository.RoomRepository
import retrofit2.HttpException

class RoomRepositoryImpl(
    private val api: MuseumApi
): RoomRepository {
    override suspend fun getRoom(url: String): Room? {
        var room: Room? = null

        try {
            room = api.getRoom(url)?.toRoom()
        } catch (e: HttpException) {
            Log.e(TAG, e.message())
        }

        return room
    }

    companion object {
        const val TAG = "RoomRepositoryImpl"
    }
}