package it.afm.artworkstracker.featureArtwork.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ArtworkAuthor(str: String) {
    Row(modifier = Modifier) {
        Text(text = str)
    }
}