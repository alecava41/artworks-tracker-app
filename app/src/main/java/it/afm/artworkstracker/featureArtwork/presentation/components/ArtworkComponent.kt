package it.afm.artworkstracker.featureArtwork.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import it.afm.artworkstracker.featureArtwork.presentation.ArtworkEvent

@Composable
fun ArtworkComponent(
    artworkName: String = "",
    artworkAuthor: String = "",
    isAudioEnabled: Boolean = false,
    onEvent: (ArtworkEvent) -> Unit,
    description: String = ""
) {
    // Button to close ??
    // Card ??
    Column {
        // Button for closing Artwork Card ??
        Row {
            Column {
                ArtworkName(str = artworkName)
                ArtworkAuthor(str = artworkAuthor)
            }
            MediaPlayer(isAudioEnabled = isAudioEnabled, onEvent = onEvent)
        }
        SlideShow(nextSlide = onEvent, previousSlide = onEvent)
        Description(desc = description)
    }
}