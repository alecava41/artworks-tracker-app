package it.afm.artworkstracker.core.presentation.components

import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.compose.animation.*
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import it.afm.artworkstracker.util.Screen
import it.afm.artworkstracker.R
import it.afm.artworkstracker.featureArtwork.presentation.components.CloseButton

@OptIn(ExperimentalComposeUiApi::class, ExperimentalAnimationApi::class)
@Composable
fun TutorialScreen(
    navController: NavController,
    tts: TextToSpeech?,
    onDialogClosed: () -> Unit
) {

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
                    .fillMaxSize(0.85f) // TODO: (side effect) if the dialog is smaller than that, then clicking on empty spots will not close it
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
                            Text(text = "Help Tutorial")
                            Column(modifier = Modifier.semantics(mergeDescendants = true) { }) {
                                Text(text = "How to begin a visit")
                                Text(text = "")
                            }
                            Column(modifier = Modifier.semantics(mergeDescendants = true) { }) {
                                Text(text = "Map")
                                Text(text = "")
                            }
                            Column(modifier = Modifier.semantics(mergeDescendants = true) { }) {
                                Text(text = "Legend")
                                Text(text = "Here there are more details about some icons:")
                                val icons = listOf(R.drawable.picture)
                                val contentDesc = listOf("")
                                val iconsDesc = listOf("")
                                for (i in icons.indices) {
                                    Row {
                                        Icon(painter = painterResource(id = icons[i]), contentDescription = contentDesc[i])
                                        Text(text = iconsDesc[i])
                                    }
                                }
                            }
                        }
                        Column(modifier = Modifier.semantics(mergeDescendants = true) { }) {
                            Text(text = "Multimedia")
                            Text(text = "")
                        }
                        Column(modifier = Modifier.semantics(mergeDescendants = true) { }) {
                            Text(text = "Visited Artworks List")
                            Text(text = "")
                        }
                        Column(modifier = Modifier.semantics(mergeDescendants = true) { }) {
                            Text(text = "How to end a visit")
                            Text(text = "")
                        }
                        CloseButton(
                            navController = navController,
                            onClick = {
                                tts?.stop()
                                onDialogClosed()
                            }
                        )
                    }
                }
            }
        }
    }
}