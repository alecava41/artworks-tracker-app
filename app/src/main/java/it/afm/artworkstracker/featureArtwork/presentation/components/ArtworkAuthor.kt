package it.afm.artworkstracker.featureArtwork.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

@Composable
fun ArtworkAuthor(str: String) {
    Row {
        Text(
            text = str,
/*            fontSize = 14.sp,
            fontStyle = FontStyle.Italic,
            fontFamily = FontFamily.Serif,*/
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.titleMedium,
        )
    }
}