package it.afm.artworkstracker.featureArtwork.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun ArtworkName(str: String) {
    Text(
        modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 15.dp),
        text = str,
        textAlign = TextAlign.Start,
        style = MaterialTheme.typography.headlineMedium,
        fontStyle = FontStyle.Italic
    )
}