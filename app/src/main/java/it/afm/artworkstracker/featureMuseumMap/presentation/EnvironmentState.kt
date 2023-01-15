package it.afm.artworkstracker.featureMuseumMap.presentation

data class EnvironmentState(
    val isBluetoothEnabled: Boolean = false,
    val isWifiEnabled: Boolean = false,
    val isLocationEnabled: Boolean = false,
    val isTourStarted: Boolean = false
)
