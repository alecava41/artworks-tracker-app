package it.afm.artworkstracker.featureMuseumMap.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
            .padding(5.dp)
    ) {
        Text(
            text = state.closerBeacon?.id.toString()
        )
    }
}