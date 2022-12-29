package it.afm.artworkstracker.featureArtwork.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import it.afm.artworkstracker.featureArtwork.domain.model.Artwork
import it.afm.artworkstracker.featureArtwork.presentation.ArtworkEvent
import it.afm.artworkstracker.featureArtwork.presentation.ArtworkViewModel
import java.util.*

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ArtworkComponent(
    artwork: Artwork,
    viewModel: ArtworkViewModel = hiltViewModel()
) {
    val pagerState = rememberPagerState(initialPage = 0)
    val state = viewModel.uiState.value
    val scrollState = rememberScrollState()

    ElevatedCard(
        modifier = Modifier
            .padding(15.dp)
            .verticalScroll(scrollState),
        colors = CardDefaults.elevatedCardColors(
            Color(
                red = 200,
                green = 233,
                blue = 254
            )
        ) // light blue = rgb(200, 233, 254), pink = rgb(255, 193, 255), default
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(fraction = 0.75f)
                        .padding(0.dp, 15.dp, 0.dp, 15.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    ArtworkName(str = artwork.title)
                    ArtworkAuthor(str = artwork.author)
                }
                MediaPlayer(
                    isAudioEnabled = state.isAudioEnabled,
                    onAudioChange = { viewModel.onEvent(ArtworkEvent.AudioChange) })
            }
            SlideShow(
                imageNumber = state.currentImagesNumber,
                pagerState = pagerState,
                onFirstSlide = { viewModel.onEvent(ArtworkEvent.FirstSlide) },
                onPreviousSlide = { viewModel.onEvent(ArtworkEvent.ImagePrevious(pagerState.currentPage)) },
                onNextSlide = { viewModel.onEvent(ArtworkEvent.ImageNext(pagerState.currentPage)) },
                onLastSlide = { viewModel.onEvent(ArtworkEvent.LastSlide) }
            )
            Description(desc = artwork.description)
        }
    }
}