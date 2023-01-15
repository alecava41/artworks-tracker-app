package it.afm.artworkstracker.featureVisitedArtworksList.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import it.afm.artworkstracker.R
import it.afm.artworkstracker.featureVisitedArtworksList.presentation.VisitedArtworksListViewModel
import it.afm.artworkstracker.util.Screen

@Composable
fun VisitedArtworksList(
    viewModel: VisitedArtworksListViewModel,
    navController: NavController,
) {

    val visitedArtworksList = viewModel.uiState.value.visitedArtworksList

    Column {
        if (visitedArtworksList.isNotEmpty()) {
            Text(
                text = stringResource(id = R.string.visited_artworks_label),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(10.dp)
            )
            LazyColumn(modifier = Modifier.padding(20.dp, 0.dp, 20.dp, 0.dp)) {
                items(items = visitedArtworksList) { artwork ->
                    Spacer(modifier = Modifier.padding(5.dp))
                    Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                        Button(
                            onClick = { navController.navigate(Screen.ArtworkScreen.route + "?artId=${artwork.id}") },
                            content = {
                                Column {
                                    Text(
                                        text = artwork.title,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontStyle = FontStyle.Italic
                                    )
                                    Text(
                                        text = artwork.author,
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        } else {
            VisitedArtWorksListEmpty()
        }
    }
}