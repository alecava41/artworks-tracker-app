package it.afm.artworkstracker.util

sealed class Screen(val route: String) {
    object MuseumMapScreen: Screen("museum_map_screen")
    object ArtworkScreen: Screen("artwork_screen")
}