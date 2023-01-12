package it.afm.artworkstracker.featureMuseumMap.presentation

import java.util.UUID

sealed class MuseumMapEvent {
    object PauseTour: MuseumMapEvent()
    object ResumeTour: MuseumMapEvent()

    data class SpeechStatus(val isSpeaking: Boolean): MuseumMapEvent()

    data class BackendServerDiscovered(val ip: String, val port: String): MuseumMapEvent()
    object BackendServerLost: MuseumMapEvent()

    object WifiConnectionAvailable: MuseumMapEvent()
    object WifiConnectionNotAvailable: MuseumMapEvent()

    object BluetoothAvailable: MuseumMapEvent()
    object BluetoothNotAvailable: MuseumMapEvent()

    object LocationAvailable: MuseumMapEvent()
    object LocationNotAvailable: MuseumMapEvent()

    data class ViewArtwork(val id: UUID): MuseumMapEvent()
}
