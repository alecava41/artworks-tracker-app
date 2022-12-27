package it.afm.artworkstracker.featureArtwork.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import it.afm.artworkstracker.featureArtwork.domain.model.Artwork
import it.afm.artworkstracker.featureArtwork.presentation.ArtworkEvent
import it.afm.artworkstracker.featureArtwork.presentation.ArtworkViewModel
import it.afm.artworkstracker.ui.theme.ArtworksTrackerTheme
import java.util.*

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ArtworkComponent(
    artwork: Artwork,
    viewModel: ArtworkViewModel = hiltViewModel()
) {
    val pagerState = rememberPagerState(initialPage = 1)

    Column {
        Row {
            Column {
                ArtworkName(str = artwork.title)
                ArtworkAuthor(str = artwork.author)
            }
            MediaPlayer(
                isAudioEnabled = viewModel.uiState.value.isAudioEnabled,
                onAudioChange = { viewModel.onEvent(ArtworkEvent.AudioChange) })
        }
        SlideShow(
            pagerState = pagerState,
            onFirstSlide = { viewModel.onEvent(ArtworkEvent.FirstSlide) },
            onPreviousSlide = { viewModel.onEvent(ArtworkEvent.ImagePrevious(pagerState.currentPage)) },
            onNextSlide = { viewModel.onEvent(ArtworkEvent.ImageNext(pagerState.currentPage)) },
            onLastSlide = { viewModel.onEvent(ArtworkEvent.LastSlide) }
        )
        Description(desc = artwork.description)
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ArtworksTrackerTheme {
        Surface {
            ArtworkComponent(
                Artwork(
                    author = "Igor Zawaleski",
                    title = "Your death",
                    description = "La Gioconda ritrae a metà figura una giovane donna con lunghi" +
                            "capelli scuri. È inquadrata di tre quarti, il busto è rivolto alla" +
                            "sua destra, il volto verso l'osservatore. Le mani sono incrociate" +
                            "in primo piano e con le braccia si appoggia a quello che sembra il" +
                            "bracciolo di una sedia.",
                    id = UUID.randomUUID()
                )
            )
        }
    }
}