package it.afm.artworkstracker.featureMuseumMap.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import it.afm.artworkstracker.R
import it.afm.artworkstracker.featureMuseumMap.presentation.MuseumMapEvent
import it.afm.artworkstracker.featureMuseumMap.presentation.MuseumMapViewModel
import it.afm.artworkstracker.featureMuseumMap.presentation.UiEvent
import it.afm.artworkstracker.util.Screen
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MuseumMapScreen(
    navController: NavController,
    viewModel: MuseumMapViewModel
) {
    val state = viewModel.museumMapState.value
    val ctx = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState)}
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Text(
                text = state.room.name,
                style = MaterialTheme.typography.titleLarge
            )
            RoomMap(
                room = state.room,
                lastBeacon = state.lastBeaconRanged,
                currentBeacon = state.currentBeaconRanged,
                onArtworkClicked = { id -> viewModel.onEvent(MuseumMapEvent.ViewArtwork(id)) }
            )
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
                                    Screen.ArtworkScreen.route
                                            + "?artId=${event.uuid}&url=${viewModel.baseUrl!!}"
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
                    else -> {}
                }
            }
        }
    }
}
