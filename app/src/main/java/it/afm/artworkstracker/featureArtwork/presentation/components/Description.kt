package it.afm.artworkstracker.featureArtwork.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun Description(desc: String) {

    Text(
        text = "Description:",
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier
            .padding(25.dp, 10.dp, 25.dp, 10.dp)
    )

    Text(
        text = desc,
        style = MaterialTheme.typography.bodyLarge,
        textAlign = TextAlign.Justify,
        modifier = Modifier
            .padding(25.dp, 0.dp, 25.dp, 15.dp)
    )
}