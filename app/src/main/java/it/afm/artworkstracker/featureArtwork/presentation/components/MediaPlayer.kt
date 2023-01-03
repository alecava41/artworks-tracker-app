package it.afm.artworkstracker.featureArtwork.presentation.components

import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
        val ctx = LocalContext.current

        if (!isAudioEnabled) {
            IconButton(
                onClick = {
                    if (tts != null) {
                        tts.speak(
                            description,
                            TextToSpeech.QUEUE_FLUSH,
                            null,
                            "..."
                        )
                    } else {
                        // TODO: test if it's working
                        Toast.makeText(ctx, R.string.tts_not_available, Toast.LENGTH_LONG).show()
                    }
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
                    contentDescription = stringResource(id = R.string.play_label),
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
                    contentDescription = stringResource(id = R.string.stop_label),
                    modifier = Modifier.size(35.dp)
                )
            }
        }
    }
}
