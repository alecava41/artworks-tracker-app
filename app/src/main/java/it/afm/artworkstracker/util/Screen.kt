package it.afm.artworkstracker.util

sealed class Screen(val route: String) {
    object MuseumMapScreen: Screen("museum_map_screen")
    object ArtworkScreen: Screen("artwork_screen")
    object BluetoothPermissionDialog: Screen("bluetooth_permission_dialog")
}