package it.afm.artworkstracker.featureArtwork.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import it.afm.artworkstracker.R

@OptIn(ExperimentalPagerApi::class)
@Composable
fun CarouselImage(
    url: String,
    beaconId: String,
    pagerState: PagerState
) {
    val context = LocalContext.current
    val placeholderImage =
        if (isSystemInDarkTheme()) R.drawable.artwork_not_available_dark_screen else R.drawable.artwork_not_available_light_screen

    val painter = rememberAsyncImagePainter(
        ImageRequest.Builder(context).data(data = "$url/api/artworks/$beaconId/media/${pagerState.currentPage + 1}")
            .apply(block = fun ImageRequest.Builder.() {
                memoryCacheKey(key = "$url/api/artworks/$beaconId/media/${pagerState.currentPage + 1}")
                    .diskCacheKey(key = "$url/api/artworks/$beaconId/media/${pagerState.currentPage + 1}")
                    .error(placeholderImage)
                    .fallback(placeholderImage)
                    .crossfade(enable = true)
                    .diskCachePolicy(CachePolicy.ENABLED)
                    .memoryCachePolicy(CachePolicy.ENABLED)
            }).build()
    )
    Image(
        painter = painter,
        contentDescription = null,
        contentScale = ContentScale.Fit,
        modifier = Modifier
            .size(250.dp, 200.dp)
    )
    val painterState = painter.state
    if (painterState is AsyncImagePainter.State.Loading)
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircularProgressIndicator()
        }
}