package it.afm.artworkstracker.core.presentation

import android.os.Build
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import it.afm.artworkstracker.R

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun BluetoothPermissionDialog(
    navController: NavController
) {
    val bluetoothPermissionsState = rememberMultiplePermissionsState(
        permissions =  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                        listOf(android.Manifest.permission.BLUETOOTH_SCAN, android.Manifest.permission.BLUETOOTH_CONNECT)
                        else listOf(android.Manifest.permission.BLUETOOTH, android.Manifest.permission.BLUETOOTH_ADMIN)
    )

    if (bluetoothPermissionsState.allPermissionsGranted) {
        Log.i("BluetoothPermissionDialog", "Bluetooth permissions ok!")
        // TODO: navigate to the next dialog
    } else {
        Card(
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.padding(10.dp,5.dp,10.dp,10.dp)
        ) {
        }
        Column {
            Icon(
                painter = painterResource(id = R.drawable.bluetooth),
                contentDescription = null,
                modifier = Modifier
                    .padding(top = 35.dp)
                    .height(70.dp)
                    .fillMaxWidth(),
                tint = MaterialTheme.colorScheme.onPrimary
            )

            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                val text = if (bluetoothPermissionsState.shouldShowRationale) "Please give me bluetooth permission!" else "..."

                Text(
                    text = "Bluetooth Permission",
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 5.dp)
                        .fillMaxWidth(),
                    style = MaterialTheme.typography.labelLarge,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = text,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 10.dp, start = 25.dp, end = 25.dp)
                        .fillMaxWidth(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            TextButton(
                onClick = { bluetoothPermissionsState.launchMultiplePermissionRequest() },

                ) {
                Text(
                    text = "Request permission",
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(top = 5.dp, bottom = 5.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}