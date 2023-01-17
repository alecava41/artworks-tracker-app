package it.afm.artworkstracker.featureArtwork.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import it.afm.artworkstracker.R
import it.afm.artworkstracker.core.presentation.components.CloseButton
import it.afm.artworkstracker.util.Screen

@Composable
fun ArtworkScreenNotAvailable(
    navController: NavController,
    onDialogClosed: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(
            text = stringResource(id = R.string.title_artwork_not_available),
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(50.dp))
        Icon(
            modifier = Modifier
                .size(120.dp)
                .align(Alignment.CenterHorizontally),
            painter = painterResource(id = R.drawable.artwork_not_available_light_screen),
            contentDescription = null
        )
        Spacer(modifier = Modifier.height(50.dp))
        Text(
            text = stringResource(id = R.string.artwork_not_available_label),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Justify,
            modifier = Modifier.padding(20.dp, 0.dp)
        )
        CloseButton(
            navController = navController,
            onClick = {
                val isMapShownBehind =
                    navController.previousBackStackEntry?.destination?.route == Screen.MuseumMapScreen.route

                if (isMapShownBehind)
                    onDialogClosed()
            }
        )
    }
}