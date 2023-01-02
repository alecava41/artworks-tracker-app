package it.afm.artworkstracker.featureArtwork.presentation.components

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import it.afm.artworkstracker.R

@Composable
fun MediaPlayer(
    isAudioEnabled: Boolean,
    onAudioChange: () -> Unit,
    description: String,
    tts: TextToSpeech,
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 0.dp, 15.dp, 0.dp)
    ) {
        if (!isAudioEnabled) {
            IconButton(
                onClick = {
                    tts.speak(
                        description,
                        TextToSpeech.QUEUE_FLUSH,
                        null,
                        null
                    )
                    onAudioChange()
                },
                modifier = Modifier.border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.onPrimary,
                    shape = RoundedCornerShape(30.dp)
                )
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.play),
                    contentDescription = "Play the audio",
                    modifier = Modifier.size(35.dp)
                )
            }
        } else {
            IconButton(
                onClick = {
                    tts.stop()
                    onAudioChange()
                },
                modifier = Modifier.border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.onPrimary,
                    shape = RoundedCornerShape(30.dp)
                )
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.stop),
                    contentDescription = "Stop the audio",
                    modifier = Modifier.size(35.dp)
                )
            }
        }
    }

    LaunchedEffect(key1 = true) {
        if (!tts.isSpeaking) {
            run { onAudioChange }
        }
    }
}
