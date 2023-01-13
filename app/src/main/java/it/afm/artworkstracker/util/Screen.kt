package it.afm.artworkstracker.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import it.afm.artworkstracker.R

sealed class Screen(
    val route: String,
    val stringId: Int?,
    val imageVector: ImageVector?
) {
    object MuseumMapScreen : Screen("museum_map_screen", R.string.map_label, Icons.Default.Map)
    object ArtworkScreen : Screen("artwork_screen", null, null)
    object VisitedArtworksListScreen : Screen("visited_artworks_list_screen", R.string.visited_list_label, Icons.Default.List)
    object SettingsScreen : Screen("settings_screen", R.string.settings_label, Icons.Default.Settings)
}