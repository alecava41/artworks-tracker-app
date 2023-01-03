package it.afm.artworkstracker.featureArtwork.presentation.components

import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import it.afm.artworkstracker.R

@Composable
fun MediaPlayer(
    isAudioEnabled: Boolean,
    onSpeechStarted: () -> Unit,
    onSpeechFinished: () -> Unit,
    description: String,
    tts: TextToSpeech?,
) {
    tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
        override fun onStart(utteranceId: String) {
            onSpeechStarted()
        }

        override fun onDone(utteranceId: String) {
            onSpeechFinished()
        }

        override fun onStop(utteranceId: String?, interrupted: Boolean) {
            if (interrupted)
                onSpeechFinished()
        }

        @Deprecated("Deprecated in Java", ReplaceWith("onSpeechFinished()"))
        override fun onError(utteranceId: String?) {
            onSpeechFinished()
        }

        override fun onError(utteranceId: String, error: Int) {
            onSpeechFinished()
        }

    })

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
                    tts?.speak(
                        description,
                        TextToSpeech.QUEUE_FLUSH,
                        null,
                        "..."
                    )
                },
                modifier = Modifier.border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.onPrimary,
                    shape = RoundedCornerShape(30.dp)
                ),
                enabled = tts != null
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
                    tts?.stop()
                },
                modifier = Modifier.border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.onPrimary,
                    shape = RoundedCornerShape(30.dp)
                ),
                enabled = tts != null
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.stop),
                    contentDescription = "Stop the audio",
                    modifier = Modifier.size(35.dp)
                )
            }
        }
    }
}
