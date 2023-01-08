package it.afm.artworkstracker.featureMuseumMap.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import it.afm.artworkstracker.R
import it.afm.artworkstracker.core.presentation.components.PermissionsNotGiven
import it.afm.artworkstracker.core.presentation.components.PermissionsRequest
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
    viewModel: MuseumMapViewModel
) {
    val state = viewModel.museumMapState.value
    val environmentState = viewModel.environmentState.value

    val ctx = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val permissionsState = rememberMultiplePermissionsState(permissions = PermissionsUtil.getPermissionsList())

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(
        key1 = lifecycleOwner,
        effect = {
            val observer = LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_START) {
                    // TODO: add environment checks? (wifi, bluetooth, location, ...)
                    // TODO: trigger acton to start to listen for beacons
                    if (!permissionsState.allPermissionsGranted)
                        permissionsState.launchMultiplePermissionRequest()
                }
            }
            lifecycleOwner.lifecycle.addObserver(observer)

            onDispose {
                lifecycleOwner.lifecycle.removeObserver(observer)
                // TODO: may trigger actions on VM (such as stop ranging, reset url, ...)
            }
        }
    )

    if (permissionsState.allPermissionsGranted) {
        Scaffold(
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
        ) {
            Column(
                modifier = Modifier
                    .padding(it)
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Text(
                    text = state.room!!.name, // TODO: fix it
                    style = MaterialTheme.typography.titleLarge
                )
                RoomMap(
                    room = state.room,
                    lastBeacon = state.lastBeaconRanged,
                    currentBeacon = state.currentBeaconRanged,
                    onArtworkClicked = { id -> viewModel.onEvent(MuseumMapEvent.ViewArtwork(id)) }
                )
            }
        }
    } else if (permissionsState.shouldShowRationale) {
        PermissionsRequest(
            onAnyButtonClicked = { permissionsState.launchMultiplePermissionRequest() }
        )
    } else {
        PermissionsNotGiven()
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
                            duration = SnackbarDuration.Long
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
