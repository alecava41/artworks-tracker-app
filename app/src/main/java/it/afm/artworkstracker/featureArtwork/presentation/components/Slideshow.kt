package it.afm.artworkstracker.featureArtwork.presentation.components

import android.annotation.SuppressLint
//import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import it.afm.artworkstracker.R
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalPagerApi::class)
@Composable
fun SlideShow(
    imageNumber: Int,
    pagerState: PagerState,
    onFirstSlide: () -> Unit,
    onPreviousSlide: () -> Unit,
    onNextSlide: () -> Unit,
    onLastSlide: () -> Unit
) {
    //Log.i("current images number", imageNumber.toString())
    val scope = rememberCoroutineScope()
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        HorizontalPager(count = 3, state = pagerState) { page ->
            Text(
                text = "Page: $page",
            )
/*            AsyncImage(
                model = "",
                contentDescription = "",
                alignment = Alignment.Center,
                contentScale = ContentScale.Fit // https://developer.android.com/reference/kotlin/androidx/compose/ui/layout/ContentScale.Companion
            )*/
            scope.launch {
                pagerState.scrollToPage(page = pagerState.currentPage)
                // TODO need to update viewModel currentImagesNumber state with pagerState.currentPage
                //Log.i("pager state", pagerState.currentPage.toString())
                //Log.i("current images number", imageNumber.toString())
            }
        }
        Row {
            IconButton(onClick = onFirstSlide) {
                Icon(
                    painter = painterResource(id = R.drawable.double_left_arrow_slideshow),
                    contentDescription = "Slide artwork's images to left",
                )
            }
            IconButton(onClick = onPreviousSlide) {
                Icon(
                    painter = painterResource(id = R.drawable.left_arrow_slideshow),
                    contentDescription = "Slide artwork's images to right",
                )
            }
            IconButton(onClick = onNextSlide) {
                Icon(
                    painter = painterResource(id = R.drawable.right_arrow_slideshow),
                    contentDescription = "Slide artwork's images to left",
                )
            }
            IconButton(onClick = onLastSlide) {
                Icon(
                    painter = painterResource(id = R.drawable.double_right_arrow_slideshow),
                    contentDescription = "Slide artwork's images to left",
                )
            }
            scope.launch {
                pagerState.scrollToPage(page = imageNumber)
            }
        }
    }
}