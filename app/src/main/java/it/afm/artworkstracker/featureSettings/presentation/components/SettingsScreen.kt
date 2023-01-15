package it.afm.artworkstracker.featureSettings.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import it.afm.artworkstracker.R
import it.afm.artworkstracker.featureSettings.presentation.SettingsEvent
import it.afm.artworkstracker.featureSettings.presentation.SettingsViewModel

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel
) {
    val state = viewModel.uiState.value

    // TODO: fix view (not really nice) (colors, button size, ...)

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            Button(
                enabled = state.isEndVisitEnabled,
                onClick = { viewModel.onEvent(SettingsEvent.DeleteArtworks) },
                content = {
                    Column {
                        Text(
                            text = stringResource(id = R.string.end_tour_button),
                            style = MaterialTheme.typography.labelMedium)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}