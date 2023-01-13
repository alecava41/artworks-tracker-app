package it.afm.artworkstracker.featureVisitedArtworksList.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import it.afm.artworkstracker.R
import it.afm.artworkstracker.featureVisitedArtworksList.presentation.VisitedArtworksListViewModel
import it.afm.artworkstracker.util.Screen

@Composable
fun VisitedArtworksList(
    viewModel: VisitedArtworksListViewModel,
    navController: NavController
) {

    val visitedArtworksList = viewModel.uiState.value.visitedArtworksList

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            if (visitedArtworksList.isNotEmpty()) {
                Text(
                    text = stringResource(id = R.string.visited_artworks_label),
                    style = MaterialTheme.typography.titleMedium
                )
                LazyColumn {
                    items(items = visitedArtworksList) { artwork ->
                        Button(
                            onClick = { navController.navigate(Screen.ArtworkScreen.route + "?artId=${artwork.id}") },
                            content = {
                                Column {
                                    Text(text = artwork.title, style = MaterialTheme.typography.labelLarge)
                                    Text(text = artwork.author, style = MaterialTheme.typography.labelSmall)
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            } else {
                Text(text = "It seems you haven't visited any artwork yet. Get closer to a beacon to visit an artwork.")
            }
        }
    }
}