package it.afm.artworkstracker.core.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import it.afm.artworkstracker.R

@Composable
fun StartTourScreen(
    onTourStarted: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(id = R.string.start_tour_title),
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(50.dp))
        Icon(
            modifier = Modifier
                .size(120.dp)
                .align(Alignment.CenterHorizontally),
            painter = painterResource(id = R.drawable.start),
            contentDescription = null
        )
        Spacer(modifier = Modifier.height(50.dp))
        Text(
            text = stringResource(id = R.string.start_tour_body),
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(50.dp))
        IconButton(
            onClick = onTourStarted,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(id = R.string.start_tour_button),
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}