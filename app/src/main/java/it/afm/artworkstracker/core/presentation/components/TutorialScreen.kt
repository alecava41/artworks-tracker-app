package it.afm.artworkstracker.core.presentation.components

import androidx.compose.animation.*
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import it.afm.artworkstracker.R
import it.afm.artworkstracker.featureArtwork.presentation.components.CloseButton

@OptIn(ExperimentalComposeUiApi::class, ExperimentalAnimationApi::class)
@Composable
fun TutorialScreen(
    navController: NavController
) {

    val scrollState = rememberScrollState()
    val transitionState = remember {
        MutableTransitionState(false).apply {
            targetState = true
        }
    }

    AnimatedVisibility(
        visibleState = transitionState,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Dialog(
            properties = DialogProperties(
                usePlatformDefaultWidth = false,
            ),
            onDismissRequest = {
                navController.navigateUp()
            }
        ) {
            Box(
                Modifier
                    .fillMaxSize(0.85f) // TODO: (side effect) if the dialog is smaller than that, then clicking on empty spots will not close it
            ) {
                Box(
                    Modifier
                        .align(Alignment.Center)
                        .animateEnterExit(
                            enter = slideInVertically(animationSpec = tween(durationMillis = 1000)),
                            exit = slideOutVertically(animationSpec = tween(durationMillis = 1000))
                        )
                        .fillMaxSize()
                ) {
                    ElevatedCard(
                        colors = CardDefaults.elevatedCardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Column {
                            Text(
                                text = stringResource(id = R.string.help_tutorial_label),
                                style = MaterialTheme.typography.headlineMedium,
                                fontStyle = FontStyle.Italic,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(25.dp, 15.dp, 25.dp, 0.dp)
                            )
                            Column(
                                modifier = Modifier
                                    .verticalScroll(scrollState)
                                    .weight(weight = 1f, fill = false)
                                    .padding(25.dp, 15.dp)
                            ) {
                                Column(modifier = Modifier.semantics(mergeDescendants = true) { }) {
                                    Text(
                                        text = stringResource(id = R.string.how_to_begin_visit_label),
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        fontStyle = FontStyle.Italic
                                    )
                                    Spacer(modifier = Modifier.padding(5.dp))
                                    Text(text = stringResource(id = R.string.how_to_begin_visit_desc_label))
                                    Spacer(modifier = Modifier.padding(10.dp))
                                }
                                Column(modifier = Modifier.semantics(mergeDescendants = true) { }) {
                                    Text(
                                        text = stringResource(id = R.string.map_label),
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        fontStyle = FontStyle.Italic
                                    )
                                    Spacer(modifier = Modifier.padding(5.dp))
                                    Text(text = stringResource(id = R.string.map_desc_label))
                                    Spacer(modifier = Modifier.padding(10.dp))
                                }
                                Column(modifier = Modifier.semantics(mergeDescendants = true) { }) {
                                    Text(
                                        text = stringResource(id = R.string.legend_label),
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        fontStyle = FontStyle.Italic
                                    )
                                    Spacer(modifier = Modifier.padding(5.dp))
                                    Text(text = stringResource(id = R.string.legend_desc_label))
                                    Spacer(modifier = Modifier.padding(5.dp))
                                    val icons = listOf(
                                        R.drawable.picture,
                                        R.drawable.sculpture,
                                        R.drawable.user,
                                        R.drawable.star,
                                        R.drawable.visit
                                    )
                                    val contentDesc = listOf(
                                        R.string.painting_label,
                                        R.string.sculpture_label,
                                        R.string.user_label,
                                        R.string.starred_artwork_label,
                                        R.string.visited_artwork_label
                                    )
                                    val iconsDesc = listOf(
                                        R.string.painting_desc_label,
                                        R.string.sculpture_desc_label,
                                        R.string.user_desc_label,
                                        R.string.starred_artwork_desc_label,
                                        R.string.visited_artwork_desc_label
                                    )
                                    for (i in icons.indices) {
                                        Spacer(modifier = Modifier.height(15.dp))
                                        Row {
                                            Icon(
                                                painter = painterResource(id = icons[i]),
                                                contentDescription = stringResource(id = contentDesc[i])
                                            )
                                            Spacer(modifier = Modifier.width(15.dp))
                                            Text(
                                                text = stringResource(id = iconsDesc[i]),
                                                style = MaterialTheme.typography.labelLarge
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.padding(10.dp))
                                }
                                Column(modifier = Modifier.semantics(mergeDescendants = true) { }) {
                                    Text(
                                        text = stringResource(id = R.string.multimedia_label),
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        fontStyle = FontStyle.Italic
                                    )
                                    Spacer(modifier = Modifier.padding(5.dp))
                                    Text(text = stringResource(id = R.string.multimedia_desc_label))
                                    Spacer(modifier = Modifier.padding(10.dp))
                                }
                                Column(modifier = Modifier.semantics(mergeDescendants = true) { }) {
                                    Text(
                                        text = stringResource(id = R.string.visited_artworks_list_label),
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        fontStyle = FontStyle.Italic
                                    )
                                    Spacer(modifier = Modifier.padding(5.dp))
                                    Text(text = stringResource(id = R.string.visited_artworks_list_desc_label))
                                    Spacer(modifier = Modifier.padding(10.dp))
                                }
                                Column(modifier = Modifier.semantics(mergeDescendants = true) { }) {
                                    Text(
                                        text = stringResource(id = R.string.how_to_end_visit_label),
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        fontStyle = FontStyle.Italic
                                    )
                                    Spacer(modifier = Modifier.padding(5.dp))
                                    Text(text = stringResource(id = R.string.how_to_end_visit_desc_label))
                                }
                            }
                            CloseButton(
                                navController = navController,
                                onClick = {}
                            )
                        }
                    }
                }
            }
        }
    }
}