package it.afm.artworkstracker.featureMuseumMap.domain.repository

import it.afm.artworkstracker.featureMuseumMap.domain.model.Room

interface RoomRepository {
    suspend fun getRoom(url: String): Room?
}