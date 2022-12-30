package it.afm.artworkstracker.featureMuseumMap.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import it.afm.artworkstracker.featureMuseumMap.presentation.MuseumMapViewModel
import it.afm.artworkstracker.featureMuseumMap.presentation.UiEvent
import it.afm.artworkstracker.util.Screen
import kotlinx.coroutines.flow.collectLatest

@Composable
fun MuseumMapScreen(
    navController: NavController,
    viewModel: MuseumMapViewModel
) {
    val state = viewModel.museumMapState.value

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Column(
            modifier = Modifier
        ) {
            Text(
                text = state.room.name,
                style = MaterialTheme.typography.titleLarge
            )
            RoomMap(
                room = state.room,
                eventFlow = viewModel.eventFlow
            )
        }

        LaunchedEffect(key1 = true) {
            viewModel.eventFlow.collectLatest { event ->
                when (event) {
                    is UiEvent.NewCloserBeacon -> {
                        navController.navigate(Screen.ArtworkScreen.route
                                + "?artId=${event.uuid}&url=${viewModel.baseUrl!!}")
                    }
                    else -> {}
                }
            }
        }
    }
}
