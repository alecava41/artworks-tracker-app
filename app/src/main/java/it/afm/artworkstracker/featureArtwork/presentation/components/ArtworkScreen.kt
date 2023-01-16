package it.afm.artworkstracker.featureArtwork.presentation.components

import android.speech.tts.TextToSpeech
import android.util.Log
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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import it.afm.artworkstracker.R
import it.afm.artworkstracker.core.presentation.components.CloseButton
import it.afm.artworkstracker.core.presentation.components.MediaPlayer
import it.afm.artworkstracker.featureArtwork.presentation.ArtworkEvent
import it.afm.artworkstracker.featureArtwork.presentation.ArtworkViewModel
import it.afm.artworkstracker.util.Screen

@OptIn(ExperimentalAnimationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun ArtworkScreen(
    navController: NavController,
    viewModel: ArtworkViewModel,
    tts: TextToSpeech?,
    onDialogClosed: () -> Unit
) {
    val vmState = viewModel.uiState.value
    val scrollState = rememberScrollState()
    val transitionState = remember {
        MutableTransitionState(false).apply {
            targetState = true
        }
    }

    AnimatedVisibility(
        visibleState = transitionState,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Dialog(
            properties = DialogProperties(
                usePlatformDefaultWidth = false,
            ),
            onDismissRequest = {
                val isMapShownBehind =
                    navController.previousBackStackEntry?.destination?.route == Screen.MuseumMapScreen.route

                Log.i("...", navController.previousBackStackEntry?.destination?.route ?: "null")

                tts?.stop()

                if (isMapShownBehind)
                    onDialogClosed()

                navController.navigateUp()
            }) {
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
                    ElevatedCard {
                        Column {
                            if (vmState.artwork != null) {
                                Column(
                                    modifier = Modifier
                                        .verticalScroll(scrollState)
                                        .weight(weight = 1f, fill = false)
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
                                            startLabel = R.string.artwork_play_label,
                                            stopLabel = R.string.artwork_stop_label,
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
                                CloseButton(
                                    navController = navController,
                                    onClick = {
                                        tts?.stop()
                                        onDialogClosed()
                                    }
                                )
                            } else {
                                ArtworkScreenNotAvailable()
                            }
                        }
                    }
                }
            }
        }
    }
}