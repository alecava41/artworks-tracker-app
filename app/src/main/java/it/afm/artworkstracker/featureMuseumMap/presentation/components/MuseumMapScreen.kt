package it.afm.artworkstracker.featureMuseumMap.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import it.afm.artworkstracker.featureMuseumMap.presentation.MuseumMapViewModel

@Composable
fun MuseumMapScreen(
    navController: NavController,
    viewModel: MuseumMapViewModel = hiltViewModel()
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
                text = state.room?.name ?: "???",
                style = MaterialTheme.typography.titleLarge
            )
            RoomMap(
                navController = navController,
                room = state.room!!,
//                eventFlow = viewModel.eventFlow
            )
        }
    }
}
