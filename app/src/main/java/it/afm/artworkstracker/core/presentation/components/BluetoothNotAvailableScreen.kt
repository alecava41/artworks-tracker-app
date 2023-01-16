package it.afm.artworkstracker.core.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import it.afm.artworkstracker.R

@Composable
fun BluetoothNotAvailableScreen(
    onBluetoothEnablingRequest: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(id = R.string.bluetooth_not_enabled_title),
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(50.dp))
        Icon(
            modifier = Modifier
                .size(120.dp)
                .align(Alignment.CenterHorizontally),
            painter = painterResource(id = R.drawable.bluetooth_disabled),
            contentDescription = null
        )
        Spacer(modifier = Modifier.height(50.dp))
        Text(
            text = stringResource(id = R.string.bluetooth_not_enabled_body),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Justify,
            modifier = Modifier.padding(20.dp, 0.dp)
        )
        Spacer(modifier = Modifier.height(50.dp))
        Button(
            onClick = onBluetoothEnablingRequest,
            content = {
                Text(
                    text = stringResource(id = R.string.bluetooth_not_enabled_button),
                    style = MaterialTheme.typography.labelLarge
                )
            }
        )
    }
}