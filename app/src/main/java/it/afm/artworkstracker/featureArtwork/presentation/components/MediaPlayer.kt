package it.afm.artworkstracker.featureArtwork.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import it.afm.artworkstracker.R

@Composable
fun MediaPlayer(
    isAudioEnabled: Boolean,
    onAudioChange: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        IconButton(
            onClick = onAudioChange,
            modifier = Modifier.border(
                width = 2.dp,
                color = Color.Black,
                shape = RoundedCornerShape(30.dp)
            )
        ) {
            if (!isAudioEnabled) {
                Icon(
                    painter = painterResource(id = R.drawable.play),
                    contentDescription = "Play the audio",
                    modifier = Modifier.size(35.dp)
                )
            } else {
                Icon(
                    painter = painterResource(id = R.drawable.stop),
                    contentDescription = "Stop the audio",
                    modifier = Modifier.size(35.dp)
                )
            }
        }
    }
}
