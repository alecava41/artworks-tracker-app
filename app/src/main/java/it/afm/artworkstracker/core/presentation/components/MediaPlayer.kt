package it.afm.artworkstracker.core.presentation.components

import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
    @StringRes startLabel: Int,
    @StringRes stopLabel: Int,
    tts: TextToSpeech?,
) {
    Log.i("MediaPlayer", "isAudioEnabled = $isAudioEnabled")

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

        //if (!isAudioEnabled) {
        IconButton(
            onClick = {
                if (tts != null) {
                    if (!isAudioEnabled) {
                        tts.speak(
                            description,
                            TextToSpeech.QUEUE_FLUSH,
                            null,
                            "..."
                        )
                    } else {
                        tts.stop()
                    }
                } else {
                    Toast.makeText(ctx, R.string.tts_not_available, Toast.LENGTH_LONG).show()
                }
            },
            modifier = Modifier
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.onSurface,
                    shape = RoundedCornerShape(30.dp)
                )
                .clip(RoundedCornerShape(30.dp))
                .background(MaterialTheme.colorScheme.onSurface),
            enabled = tts != null
        ) {
            val iconMediaPlayer = if (!isAudioEnabled) R.drawable.play else R.drawable.stop
            Icon(
                painter = painterResource(id = iconMediaPlayer),
                tint = MaterialTheme.colorScheme.surface,
                contentDescription = stringResource(id = startLabel),
                modifier = Modifier.size(35.dp)
            )
            Log.e("isAudioEnabled:", isAudioEnabled.toString())
        }
        /*} else {
            IconButton(
                onClick = {
                    tts?.stop()
                },
                modifier = Modifier
                    .border(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.onSurface,
                        shape = RoundedCornerShape(30.dp)
                    )
                    .clip(RoundedCornerShape(30.dp))
                    .background(MaterialTheme.colorScheme.onSurface),
                enabled = tts != null
            ) {
                Icon(
                    painter = painterResource(id = ),
                    tint = MaterialTheme.colorScheme.surface,
                    contentDescription = stringResource(id = stopLabel),
                    modifier = Modifier.size(35.dp)
                )
            }
        }*/
    }
}
