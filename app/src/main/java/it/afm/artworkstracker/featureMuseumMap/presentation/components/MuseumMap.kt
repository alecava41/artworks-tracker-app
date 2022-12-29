package it.afm.artworkstracker.featureMuseumMap.presentation.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import it.afm.artworkstracker.R
import it.afm.artworkstracker.featureMuseumMap.domain.model.Room
import it.afm.artworkstracker.featureMuseumMap.domain.util.ArtworkType
import it.afm.artworkstracker.featureMuseumMap.domain.util.PerimeterEntity
import it.afm.artworkstracker.featureMuseumMap.domain.util.Side
import it.afm.artworkstracker.featureMuseumMap.presentation.UiEvent
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.UUID

@Composable
fun RoomMap(
    navController: NavController,
    room: Room,
) {
    var scale = 1f

    val animationScope = rememberCoroutineScope()
    val animX = remember { Animatable(initialValue = 0f) }
    val animY = remember { Animatable(initialValue = 0f) }

    val state = rememberTransformableState { zoomChange, _, _ ->
        scale *= zoomChange
    }

    val picturePainter = rememberVectorPainter(image = ImageVector.vectorResource(id = R.drawable.picture))
    val sculpturePainter = rememberVectorPainter(image = ImageVector.vectorResource(id = R.drawable.sculpture))
    val userPainter = rememberVectorPainter(image = ImageVector.vectorResource(id = R.drawable.user))
    val starredPainter = rememberVectorPainter(image = ImageVector.vectorResource(id = R.drawable.star))
    val visitedPainter = rememberVectorPainter(image = ImageVector.vectorResource(id = R.drawable.visit))

    val artworkPositions = arrayListOf<Pair<UUID, Rect>>()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .horizontalScroll(rememberScrollState())
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
            .transformable(state = state)
            .graphicsLayer(
                scaleX = scale,
                scaleY = scale,
            )

    ) {
//        LaunchedEffect(key1 = true) {
//            eventFlow.collectLatest { event ->
//                when (event) {
//                    is UiEvent.NewCloserBeacon -> {
//                        val artworkPosition = artworkPositions.find { it.first == event.uuid }!!
//
////                        val offset = when (room.artworks.find { it.beacon == artworkPosition.first }!!.side) {
////                            Side.LEFT -> Offset(
////                                x = artworkPosition.second.left - userSize.toPx() - userDistanceFromArtwork.toPx(),
////                                y = artworkPosition.second.top + artworkSize.toPx() / 2 - userSize.toPx() / 2
////                            )
////                            Side.RIGHT -> Offset(
////                                x = artworkPosition.second.right + userDistanceFromArtwork.toPx(),
////                                y = artworkPosition.second.top + artworkSize.toPx() / 2 - userSize.toPx() / 2
////                            )
////                            Side.UP -> Offset(
////                                x = artworkPosition.second.left + artworkSize.toPx() / 2 - userSize.toPx() / 2,
////                                y = artworkPosition.second.top - userSize.toPx() - userDistanceFromArtwork.toPx()
////                            )
////                            Side.DOWN -> Offset(
////                                x = artworkPosition.second.left + artworkSize.toPx() / 2 - userSize.toPx() / 2,
////                                y = artworkPosition.second.bottom + userDistanceFromArtwork.toPx()
////                            )
////                        }
//
//                        val offset = when (room.artworks.find { it.beacon == artworkPosition.first }!!.side) {
//                            Side.LEFT -> Offset(
//                                x = artworkPosition.second.left ,
//                                y = artworkPosition.second.top
//                            )
//                            Side.RIGHT -> Offset(
//                                x = artworkPosition.second.right,
//                                y = artworkPosition.second.top
//                            )
//                            Side.UP -> Offset(
//                                x = artworkPosition.second.left,
//                                y = artworkPosition.second.top
//                            )
//                            Side.DOWN -> Offset(
//                                x = artworkPosition.second.left,
//                                y = artworkPosition.second.bottom
//                            )
//                        }
//
//                        animX.animateTo(
//                            targetValue = offset.x,
//                            animationSpec = tween(1000)
//                        )
//
//                        animY.animateTo(
//                            targetValue = offset.y,
//                            animationSpec = tween(1000)
//                        )
//
//                        delay(500L)
//
////                        navController.navigate()
//                    }
//                }
//            }
//        }

        Canvas(
            modifier = Modifier
                .align(Alignment.Center)
                .size(1000.dp, 1000.dp)
                .align(Alignment.Center)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            val art = artworkPositions.find { pos -> pos.second.contains(it) }
                            if (art != null) {
                                animationScope.launch {
                                    animX.animateTo(
                                        targetValue = art.second.left,
                                        animationSpec = tween(1000)
                                    )

                                    animY.animateTo(
                                        targetValue = art.second.top,
                                        animationSpec = tween(1000)
                                    )
                                }
                            }
                        }
                    )
                }
        ) {
            // Draw the room's perimeter
            val groundPath = Path()

            val path = Path().apply {
                room.perimeter.forEach {
                    when (it.first) {
                        PerimeterEntity.LINE -> {
                            groundPath.lineTo(it.second.dp.toPx(), it.third.dp.toPx())
                            lineTo(it.second.dp.toPx(), it.third.dp.toPx())
                        }
                        PerimeterEntity.MOVE -> moveTo(it.second.dp.toPx(), it.third.dp.toPx())
                    }
                }
            }

            drawPath(
                path = groundPath,
                color = Color.Gray
            )

            drawPath(
                path = path,
                color = Color.Black,
                style = Stroke(10.dp.toPx()),
            )

            clipPath(path = path) {}

            // Draw artworks
            room.artworks.forEach {
                artworkPositions.add(
                    Pair(
                        it.beacon,
                        Rect(
                            offset = Offset(x = it.posX.dp.toPx(), y = it.posY.dp.toPx()),
                            size = Size(width = artworkSize.toPx(), artworkSize.toPx())
                        )
                    )
                )

                val painter = when (it.type) {
                    ArtworkType.PICTURE -> picturePainter
                    ArtworkType.SCULPTURE -> sculpturePainter
                }

                translate(
                    left = it.posX.dp.toPx(),
                    top = it.posY.dp.toPx()
                ) {
                    with(painter) {
                        draw(Size(artworkSize.toPx(), artworkSize.toPx()))
                    }
                }

                if (it.starred) {
                    translate(
                        left = it.posX.dp.toPx() + artworkSize.toPx() - starredDistanceLeft.toPx(),
                        top = it.posY.dp.toPx() - starredDistanceTop.toPx()
                    ) {
                        with(starredPainter) {
                            draw(Size(starredSize.toPx(), starredSize.toPx()))
                        }
                    }
                }

                if (it.visited) {
                    translate(
                        left = it.posX.dp.toPx() + artworkSize.toPx() - visitedDistanceLeft.toPx(),
                        top = it.posY.dp.toPx() + artworkSize.toPx() - visitedDistanceTop.toPx()
                    ) {
                        with(visitedPainter) {
                            draw(Size(visitedSize.toPx(), visitedSize.toPx()))
                        }
                    }
                }
            }

            // Draw user
            translate(
                left = animX.value,
                top = animY.value
            ) {
                with(userPainter) {
                    draw(Size(userSize.toPx(), userSize.toPx()))
                }
            }
        }
    }
}

val artworkSize = 50.dp

val starredSize = 20.dp
val starredDistanceTop = 5.dp
val starredDistanceLeft = 15.dp

val visitedSize = 40.dp
val visitedDistanceTop = 30.dp
val visitedDistanceLeft = 15.dp

val userSize = 30.dp
val userDistanceFromArtwork = 5.dp



