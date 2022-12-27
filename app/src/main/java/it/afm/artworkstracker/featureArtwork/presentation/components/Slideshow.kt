package it.afm.artworkstracker.featureArtwork.presentation.components

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import it.afm.artworkstracker.R
import it.afm.artworkstracker.featureArtwork.presentation.ArtworkEvent
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalPagerApi::class)
@Composable
fun SlideShow(
    pagerState: PagerState,
    onFirstSlide: () -> Unit,
    onPreviousSlide: () -> Unit,
    onNextSlide: () -> Unit,
    onLastSlide: () -> Unit
) {
    val scope = rememberCoroutineScope()
    Column {
        HorizontalPager(count = 4, state = pagerState) { page ->
            Text(
                text = "Page: $page",
                modifier = Modifier.fillMaxWidth()
            )
            scope.launch {
                pagerState.scrollToPage(page = pagerState.currentPage)
            }
        }
        Row {
            IconButton(onClick = onFirstSlide) {
                Icon(
                    painter = painterResource(id = R.drawable.double_left_arrow_slideshow),
                    contentDescription = "Slide artwork's images to left",
                    Modifier.size(100.dp)
                )
            }
            IconButton(onClick = onPreviousSlide) {
                Icon(
                    painter = painterResource(id = R.drawable.left_arrow_slideshow),
                    contentDescription = "Slide artwork's images to right",
                    Modifier.size(100.dp)
                )
            }
            IconButton(onClick = onNextSlide) {
                Icon(
                    painter = painterResource(id = R.drawable.right_arrow_slideshow),
                    contentDescription = "Slide artwork's images to left",
                    Modifier.size(100.dp)
                )
            }
            IconButton(onClick = onLastSlide) {
                Icon(
                    painter = painterResource(id = R.drawable.double_right_arrow_slideshow),
                    contentDescription = "Slide artwork's images to left",
                    Modifier.size(100.dp)
                )
            }
        }
    }
}