package it.afm.artworkstracker.featureMuseumMap.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import it.afm.artworkstracker.featureMuseumMap.presentation.MuseumMapViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun MuseumMapScreen(
    viewModel: MuseumMapViewModel = viewModel()
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
                text = state.closestBeacon?.id.toString()
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = state.room?.name.toString()
            )
        }
    }
}