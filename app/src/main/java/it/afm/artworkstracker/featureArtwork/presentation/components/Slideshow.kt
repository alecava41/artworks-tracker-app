package it.afm.artworkstracker.featureArtwork.presentation.components

import android.annotation.SuppressLint
import android.util.Log
//import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import it.afm.artworkstracker.R
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalPagerApi::class)
@Composable
fun SlideShow() {

    val pagerState = rememberPagerState(initialPage = 0)
    val scope = rememberCoroutineScope()
    var currentSlide: Int


    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        HorizontalPager(count = 3, state = pagerState) { numberImage ->
/*            Text(
                text = "Page: $page",
            )*/
            if (pagerState.currentPage == 0)
                AsyncImage(
                    model = "https://www.shutterstock.com/image-vector/black-share-icons-set-social-600w-1911782200.jpg",
                    contentDescription = "",
                    modifier = Modifier.size(250.dp, 200.dp),
                    alignment = Alignment.Center,
                    contentScale = ContentScale.Fit // https://developer.android.com/reference/kotlin/androidx/compose/ui/layout/ContentScale.Companion
                )
            if (pagerState.currentPage == 1)
                AsyncImage(
                    model = "https://www.shutterstock.com/image-vector/link-flat-icon-illustration-vector-600w-1551448580.jpg",
                    contentDescription = "",
                    modifier = Modifier.size(250.dp, 200.dp),
                    alignment = Alignment.Center,
                    contentScale = ContentScale.Fit // https://developer.android.com/reference/kotlin/androidx/compose/ui/layout/ContentScale.Companion
                )
            if (pagerState.currentPage == 2)
                AsyncImage(
                    model = "https://www.shutterstock.com/image-vector/hand-cursor-vector-icon-blue-600w-1628240461.jpg",
                    contentDescription = "",
                    modifier = Modifier.size(250.dp, 200.dp),
                    alignment = Alignment.Center,
                    contentScale = ContentScale.Fit // https://developer.android.com/reference/kotlin/androidx/compose/ui/layout/ContentScale.Companion
                )
            scope.launch {
                pagerState.scrollToPage(page = pagerState.currentPage)
                currentSlide = pagerState.currentPage
            }
        }
        Row(modifier = Modifier.padding(0.dp, 10.dp, 0.dp, 0.dp)) {
            IconButton(onClick = {
                currentSlide = if (pagerState.currentPage > 0) pagerState.currentPage - 1 else 2
                scope.launch { pagerState.scrollToPage(page = currentSlide) }
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.left_arrow_slideshow),
                    contentDescription = "Slide artwork's images to left",
                )
            }
            IconButton(onClick = {
                currentSlide = if (pagerState.currentPage < 2) pagerState.currentPage + 1 else 0
                scope.launch { pagerState.scrollToPage(page = currentSlide) }
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.right_arrow_slideshow),
                    contentDescription = "Slide artwork's images to right",
                )
            }
        }
    }
}