package it.afm.artworkstracker.featureMuseumMap.domain.useCase

import it.afm.artworkstracker.featureMuseumMap.domain.model.Room
import it.afm.artworkstracker.featureMuseumMap.domain.repository.RoomRepository
import java.util.UUID

class GetRoomUseCase(
    private val repository: RoomRepository
) {
    suspend operator fun invoke(beacon: UUID, baseUrl: String): Room? {
        return repository.getRoom("$baseUrl/api/rooms/?beacon=$beacon")
    }
}