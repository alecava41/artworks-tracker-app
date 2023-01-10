package it.afm.artworkstracker.featureArtwork.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import it.afm.artworkstracker.R
import it.afm.artworkstracker.util.Screen
import kotlinx.coroutines.launch

@Composable
fun CloseButton(
    navController: NavController
) {
    val scope = rememberCoroutineScope()
    Box(modifier = Modifier
        .padding(5.dp)
        .fillMaxWidth()) {
        IconButton(
            onClick = { scope.launch { navController.navigate(Screen.MuseumMapScreen.route) } },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(13.dp))
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.primary
                )
                .background(MaterialTheme.colorScheme.primaryContainer)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.close_button),
                    contentDescription = null
                )
                Text(
                    text = stringResource(id = R.string.close_label),
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}