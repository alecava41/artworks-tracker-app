package it.afm.artworkstracker.featureMuseumMap.presentation.components

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import it.afm.artworkstracker.R
import it.afm.artworkstracker.core.presentation.components.*
import it.afm.artworkstracker.featureMuseumMap.presentation.MuseumMapEvent
import it.afm.artworkstracker.featureMuseumMap.presentation.MuseumMapViewModel
import it.afm.artworkstracker.featureMuseumMap.presentation.UiEvent
import it.afm.artworkstracker.util.PermissionsUtil
import it.afm.artworkstracker.util.Screen
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun MuseumMapScreen(
    navController: NavController,
    tts: TextToSpeech?,
    onBluetoothEnableRequest: () -> Unit,
    onLocationEnableRequest: () -> Unit,
    viewModel: MuseumMapViewModel
) {
    val state = viewModel.museumMapState.value
    val environmentState = viewModel.environmentState.value

    val ctx = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val permissionsState = rememberMultiplePermissionsState(permissions = PermissionsUtil.getPermissionsList())

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxWidth()
                .background(Color(0xFFffffff))
        ) {
            if (permissionsState.allPermissionsGranted) {
                if (environmentState.isWifiEnabled) {
                    if (environmentState.isLocationEnabled) {
                        if (environmentState.isBluetoothEnabled) {
                            MuseumMap(
                                room = state.room,
                                currentArtwork = state.currentArtwork,
                                lastArtwork = state.lastArtwork,
                                tts = tts,
                                isAudioEnabled = state.isAudioEnabled,
                                onSpeechStarted = { viewModel.onEvent(MuseumMapEvent.SpeechStatus(isSpeaking = true)) },
                                onSpeechFinished = { viewModel.onEvent(MuseumMapEvent.SpeechStatus(isSpeaking = false)) },
                                onArtworkClicked = { id -> viewModel.onEvent(MuseumMapEvent.ViewArtwork(id)) }
                            )
                        } else
                            BluetoothNotAvailableScreen {
                                onBluetoothEnableRequest()
                            }
                    } else
                        LocationNotAvailableScreen {
                            onLocationEnableRequest()
                        }
                } else
                    BackendServerNotAvailableScreen()
            } else if (permissionsState.shouldShowRationale) {
                PermissionsRequestScreen(
                    onRequestPermissionButtonClick = { permissionsState.launchMultiplePermissionRequest() }
                )
            } else PermissionsNotGiven()
        }
    }

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is UiEvent.NewCloserBeacon -> {
                    navController.navigate(
                        Screen.ArtworkScreen.route
                                + "?artId=${event.uuid}&url=${viewModel.baseUrl!!}"
                    )
                }
                is UiEvent.NewCloserBeaconAlreadyVisited -> {
                    scope.launch {
                        val res = snackbarHostState.showSnackbar(
                            message = ctx.getString(R.string.snackbar_old_artwork_label),
                            actionLabel = ctx.getString(R.string.snackbar_old_artwork_action),
                            withDismissAction = true,
                            duration = SnackbarDuration.Indefinite
                        )

                        if (res == SnackbarResult.ActionPerformed) {
                            navController.navigate(
                                Screen.ArtworkScreen.route
                                        + "?artId=${event.uuid}&url=${viewModel.baseUrl!!}"
                            )
                        }
                    }
                }
                is UiEvent.ArtworkAlreadyVisitedClicked -> {
                    scope.launch {
                        val res = snackbarHostState.showSnackbar(
                            message = ctx.getString(R.string.snackbar_old_artwork_label),
                            actionLabel = ctx.getString(R.string.snackbar_old_artwork_action),
                            withDismissAction = true,
                            duration = SnackbarDuration.Short
                        )

                        if (res == SnackbarResult.ActionPerformed) {
                            navController.navigate(
                                Screen.ArtworkScreen.route + "?artId=${event.uuid}&url=${viewModel.baseUrl!!}"
                            )
                        }
                    }
                }
                is UiEvent.ArtworkNotVisitedClicked -> {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = ctx.getString(R.string.snackbar_unseen_artwork_label),
                            actionLabel = null,
                            withDismissAction = true,
                            duration = SnackbarDuration.Short
                        )
                    }
                }
            }
        }
    }
}
