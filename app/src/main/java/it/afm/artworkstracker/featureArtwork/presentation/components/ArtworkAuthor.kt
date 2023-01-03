package it.afm.artworkstracker.featureArtwork.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextAlign

@Composable
fun ArtworkAuthor(str: String) {
    Row {
        Text(
            text = str,
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.titleMedium,
        )
    }
}