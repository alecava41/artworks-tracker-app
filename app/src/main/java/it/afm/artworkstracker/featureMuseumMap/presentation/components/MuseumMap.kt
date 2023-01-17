package it.afm.artworkstracker.featureMuseumMap.presentation.components

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import it.afm.artworkstracker.core.presentation.components.MediaPlayer
import it.afm.artworkstracker.core.presentation.components.NoRoomScreen
import it.afm.artworkstracker.featureMuseumMap.domain.model.ArtworkBeacon
import it.afm.artworkstracker.featureMuseumMap.domain.model.Room
import it.afm.artworkstracker.R
import java.util.*

@Composable
fun MuseumMap(
    room: Room?,
    currentArtwork: ArtworkBeacon?,
    lastArtwork: ArtworkBeacon?,
    tts: TextToSpeech?,
    scale: MutableState<Float>,
    isAudioEnabled: Boolean,
    onSpeechStarted: () -> Unit,
    onSpeechFinished: () -> Unit,
    onArtworkClicked: (UUID) -> Unit

) {
    if (room == null) {
        NoRoomScreen()
    } else {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(fraction = 0.75f)
                        .padding(25.dp, 15.dp, 0.dp, 20.dp)
                        .semantics(mergeDescendants = true) { },
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = room.name,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
                MediaPlayer(
                    isAudioEnabled = isAudioEnabled,
                    description = currentArtwork?.direction ?: "",
                    tts = tts,
                    label = R.string.path_label,
                    onSpeechFinished = onSpeechFinished,
                    onSpeechStarted = onSpeechStarted
                )
            }
            RoomMap(
                scale = scale,
                room = room,
                lastArtwork = lastArtwork,
                currentArtwork = currentArtwork,
                onArtworkClicked = onArtworkClicked
            )
        }
    }
}