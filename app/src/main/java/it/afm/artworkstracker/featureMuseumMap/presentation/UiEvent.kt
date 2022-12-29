package it.afm.artworkstracker.featureMuseumMap.presentation

import java.util.UUID

sealed class UiEvent {
    data class NewCloserBeacon(val uuid: UUID, val alreadyVisited: Boolean): UiEvent()
}
