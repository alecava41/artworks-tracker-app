package it.afm.artworkstracker.featureMuseumMap.presentation

import java.util.UUID

sealed class MuseumMapEvent {
    object PauseTour: MuseumMapEvent()
    object ResumeTour: MuseumMapEvent()
    data class BackendServerDiscovered(val ip: String, val port: String): MuseumMapEvent()
    data class ViewArtwork(val id: UUID): MuseumMapEvent()
}
