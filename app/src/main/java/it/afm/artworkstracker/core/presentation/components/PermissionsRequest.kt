package it.afm.artworkstracker.core.presentation.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import it.afm.artworkstracker.R

@Composable
fun PermissionsRequest(
    onAnyButtonClicked: () -> Unit
) {
    AlertDialog(
        onDismissRequest = {},
        confirmButton = {
            Button(
                onClick = onAnyButtonClicked,
            ) {
                Text(
                    text = stringResource(id = R.string.permission_button),
                    style = MaterialTheme.typography.labelLarge,
                )
            }
        },
        title = {
            Text(
                text = stringResource(id = R.string.permission_title),
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Text(
                text = stringResource(id = R.string.permission_body),
                style = MaterialTheme.typography.bodyMedium
            )
        },
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        textContentColor = MaterialTheme.colorScheme.onPrimaryContainer
    )
}