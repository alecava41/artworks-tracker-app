package it.afm.artworkstracker.featureArtwork.presentation.components

import android.speech.tts.TextToSpeech
import androidx.compose.animation.*
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import it.afm.artworkstracker.featureArtwork.presentation.ArtworkEvent
import it.afm.artworkstracker.featureArtwork.presentation.ArtworkViewModel

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ArtworkScreen(
    navController: NavController,
    viewModel: ArtworkViewModel = hiltViewModel(),
    tts: TextToSpeech
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
        Box(
            Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            Box(
                Modifier
                    .align(Alignment.Center)
                    .animateEnterExit(
                        enter = slideInVertically(animationSpec = tween(durationMillis = 1000)),
                        exit = slideOutVertically(animationSpec = tween(durationMillis = 1000))
                    )
                    .sizeIn(minWidth = 256.dp, minHeight = 64.dp)
                    .background(Color.White)
            ) {
                ElevatedCard(
                    modifier = Modifier
                        .padding(15.dp)
                        .verticalScroll(scrollState),
                    colors = CardDefaults.elevatedCardColors(
                        Color(
                            red = 200,
                            green = 233,
                            blue = 254
                        )
                    ) // light blue = rgb(200, 233, 254), pink = rgb(255, 193, 255), default
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth(fraction = 0.75f)
                                    .padding(25.dp, 15.dp, 0.dp, 20.dp),
                                horizontalAlignment = Alignment.Start,
                                verticalArrangement = Arrangement.Center
                            ) {
                                ArtworkName(str = vmState.artwork.title)
                                ArtworkAuthor(str = vmState.artwork.author)
                            }
                            MediaPlayer(
                                isAudioEnabled = vmState.isAudioEnabled,
                                onAudioChange = { viewModel.onEvent(ArtworkEvent.AudioChange) },
                                description = viewModel.uiState.value.artwork.description,
                                tts = tts
                            )
                        }
                        SlideShow()
                        Description(desc = vmState.artwork.description)
                    }
                }
            }
        }
    }
}