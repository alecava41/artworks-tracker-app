package it.afm.artworkstracker.featureArtwork.presentation.components

import android.speech.tts.TextToSpeech
import androidx.compose.animation.*
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import it.afm.artworkstracker.core.presentation.components.MediaPlayer
import it.afm.artworkstracker.featureArtwork.presentation.ArtworkEvent
import it.afm.artworkstracker.featureArtwork.presentation.ArtworkViewModel

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ArtworkScreen(
    navController: NavController,
    viewModel: ArtworkViewModel,
    tts: TextToSpeech?
) {
    val vmState = viewModel.uiState.value
    val scrollState = rememberScrollState()
    val transitionState = remember {
        MutableTransitionState(false).apply {
            targetState = true
        }
    }

    // TODO: add "Close" button at the end of the card (not scrollable, fixed at the end of the card)

    AnimatedVisibility(
        visibleState = transitionState,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(
            Modifier
                .fillMaxSize(0.85f)
        ) {
            Box(
                Modifier
                    .align(Alignment.Center)
                    .animateEnterExit(
                        enter = slideInVertically(animationSpec = tween(durationMillis = 1000)),
                        exit = slideOutVertically(animationSpec = tween(durationMillis = 1000))
                    )
                    .fillMaxSize()
            ) {
                ElevatedCard(
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column {
                        Column(
                            modifier = Modifier
                                .verticalScroll(scrollState)
                                .weight(weight =1f, fill = false)
                        ) {
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
                                    ArtworkName(str = vmState.artwork.title)
                                    ArtworkAuthor(str = vmState.artwork.author)
                                }
                                MediaPlayer(
                                    isAudioEnabled = vmState.isAudioEnabled,
                                    description = vmState.artwork.description,
                                    tts = tts,
                                    onSpeechFinished = { viewModel.onEvent(ArtworkEvent.SpeechStatus(isSpeaking = false)) },
                                    onSpeechStarted = { viewModel.onEvent(ArtworkEvent.SpeechStatus(isSpeaking = true)) }
                                )
                            }
                            SlideShow(
                                url = viewModel.url,
                                beaconId = vmState.artwork.id.toString()
                            )
                            Description(desc = vmState.artwork.description)
                        }
                        CloseButton(navController = navController)
                    }
                }
            }
        }
    }
}