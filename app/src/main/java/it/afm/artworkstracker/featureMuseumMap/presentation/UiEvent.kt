package it.afm.artworkstracker.featureMuseumMap.presentation

import java.util.UUID

sealed class UiEvent {
    data class NewUserPosition(val uuid: UUID): UiEvent()
    data class NewCloserBeacon(val uuid: UUID): UiEvent()
    data class NewCloserBeaconAlreadyVisited(val uuid: UUID): UiEvent()
}
