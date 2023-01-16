package it.afm.artworkstracker.core.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import it.afm.artworkstracker.R

@Composable
fun PermissionsRequestScreen(
    onRequestPermissionButtonClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(id = R.string.permissions_require_title),
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(50.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.size(40.dp),
                painter = painterResource(id = R.drawable.bluetooth),
                contentDescription = null
            )
            Text(
                text = stringResource(id = R.string.permissions_require_bluetooth),
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.size(40.dp),
                painter = painterResource(id = R.drawable.location),
                contentDescription = null
            )
            Text(
                text = stringResource(id = R.string.permissions_require_location),
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.height(50.dp))

        Button(
            onClick = onRequestPermissionButtonClick,
            content = {
                Text(
                    text = stringResource(id = R.string.permissions_require_button),
                    style = MaterialTheme.typography.labelLarge
                )
            }
        )
    }
}

@Composable
@Preview
fun PermissionRequestScreenPreview() {
    PermissionsRequestScreen {}
}