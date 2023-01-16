package it.afm.artworkstracker.featureSettings.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import it.afm.artworkstracker.R
import it.afm.artworkstracker.featureSettings.presentation.SettingsEvent
import it.afm.artworkstracker.featureSettings.presentation.SettingsViewModel

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onTourDestroyed: () -> Unit,
) {
    val state = viewModel.uiState.value

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = stringResource(id = R.string.settings_label),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(10.dp)
        )
        Column(modifier = Modifier.padding(20.dp, 0.dp, 20.dp, 0.dp)) {
            Button(
                enabled = state.isEndVisitEnabled,
                onClick = {
                    viewModel.onEvent(SettingsEvent.DeleteArtworks)
                    onTourDestroyed()
                },
                content = {
                    Column {
                        Text(
                            text = stringResource(id = R.string.end_tour_button),
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}