package it.afm.artworkstracker.util

sealed class Screen(val route: String) {
    object MuseumMapScreen: Screen("MuseumMapScreen")
    object ArtworkScreen: Screen("ArtworkScreen")
}