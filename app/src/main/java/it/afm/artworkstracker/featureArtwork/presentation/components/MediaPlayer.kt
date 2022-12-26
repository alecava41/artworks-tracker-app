package it.afm.artworkstracker.featureArtwork.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import it.afm.artworkstracker.R
import it.afm.artworkstracker.featureArtwork.presentation.ArtworkEvent

@Composable
fun MediaPlayer(
    isAudioEnabled: Boolean,
    onEvent: (ArtworkEvent) -> Unit
) {
    Row {
        IconButton(onClick = { onEvent(ArtworkEvent.AudioChange) }) {
            if (!isAudioEnabled) {
                Icon(
                    painter = painterResource(id = R.drawable.play),
                    contentDescription = "Play the audio",
                    Modifier.size(100.dp)
                )
            } else {
                Icon(
                    painter = painterResource(id = R.drawable.stop),
                    contentDescription = "Stop the audio",
                    Modifier.size(100.dp)
                )
            }
        }
    }
}