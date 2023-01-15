package it.afm.artworkstracker.featureArtwork.presentation.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.imageLoader
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import it.afm.artworkstracker.R
import kotlinx.coroutines.launch
import java.util.*

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalPagerApi::class)
@Composable
fun SlideShow(
    url: String,
    beaconId: String
) {

    val pagerState = rememberPagerState(initialPage = 0)
    val scope = rememberCoroutineScope()
    var currentSlide: Int

    val context = LocalContext.current
    val placeholderImage = R.drawable.artwork_not_available
    val imageRequest = ImageRequest.Builder(context)
        .data("$url/api/artworks/$beaconId/media/${pagerState.currentPage + 1}")
        .memoryCacheKey(key = "$url/api/artworks/$beaconId/media/${pagerState.currentPage + 1}")
        .diskCacheKey(key = "$url/api/artworks/$beaconId/media/${pagerState.currentPage + 1}")
        .placeholder(placeholderImage)
        .error(placeholderImage)
        .fallback(placeholderImage)
        .crossfade(enable = true)
        .diskCachePolicy(CachePolicy.ENABLED)
        .memoryCachePolicy(CachePolicy.ENABLED)
        .build()

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        HorizontalPager(count = 3, state = pagerState) {
            AsyncImage(
                model = imageRequest,
                contentDescription = null,
                modifier = Modifier.size(250.dp, 200.dp),
                alignment = Alignment.Center,
                contentScale = ContentScale.Fit,
                imageLoader = context.imageLoader
            )
            scope.launch {
                pagerState.scrollToPage(page = pagerState.currentPage)
                currentSlide = pagerState.currentPage
            }
        }
        Row(
            modifier = Modifier
                .padding(0.dp, 10.dp, 0.dp, 0.dp)
                .semantics(mergeDescendants = true) { }
        ) {
            IconButton(onClick = {
                currentSlide = if (pagerState.currentPage > 0) pagerState.currentPage - 1 else 2
                scope.launch { pagerState.scrollToPage(page = currentSlide) }
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.left_arrow_slideshow),
                    contentDescription = stringResource(id = R.string.slide_left_label),
                )
            }
            IconButton(onClick = {
                currentSlide = if (pagerState.currentPage < 2) pagerState.currentPage + 1 else 0
                scope.launch { pagerState.scrollToPage(page = currentSlide) }
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.right_arrow_slideshow),
                    contentDescription = stringResource(id = R.string.slide_right_label),
                )
            }
        }
    }
}