package it.afm.artworkstracker.featureArtwork.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import it.afm.artworkstracker.R
import it.afm.artworkstracker.featureArtwork.presentation.ArtworkEvent

@Composable
fun SlideShow(
    nextSlide: (ArtworkEvent) -> Unit,
    previousSlide: (ArtworkEvent) -> Unit
) {
    Row() {
        IconButton(onClick = {nextSlide(ArtworkEvent.ImageNext)}) {
            Icon(painter = painterResource(id = R.drawable.left_arrow_slideshow), contentDescription = "Slide artwork's images to left", Modifier.size(100.dp))
        }
        //Image()
        IconButton(onClick = {previousSlide(ArtworkEvent.ImagePrevious)}) {
            Icon(painter = painterResource(id = R.drawable.play), contentDescription = "Slide artwork's images to right", Modifier.size(100.dp))
        }
    }
}