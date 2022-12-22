package it.afm.artworkstracker.featureMuseumMap.presentation

sealed class MuseumMapEvent {
    object PauseTour: MuseumMapEvent()
    object ResumeTour: MuseumMapEvent()
}
