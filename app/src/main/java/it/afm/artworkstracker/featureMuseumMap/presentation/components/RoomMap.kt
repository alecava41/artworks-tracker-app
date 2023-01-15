package it.afm.artworkstracker.featureMuseumMap.presentation.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.graphics.vector.RenderVectorGroup
import androidx.compose.ui.graphics.vector.RootGroupName
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import it.afm.artworkstracker.R
import it.afm.artworkstracker.featureMuseumMap.domain.model.ArtworkBeacon
import it.afm.artworkstracker.featureMuseumMap.domain.model.Room
import it.afm.artworkstracker.featureMuseumMap.domain.util.ArtworkType
import it.afm.artworkstracker.featureMuseumMap.domain.util.PerimeterEntity
import it.afm.artworkstracker.featureMuseumMap.domain.util.Side
import java.util.*

@Composable
fun RoomMap(
    room: Room,
    currentArtwork: ArtworkBeacon? = null,
    lastArtwork: ArtworkBeacon? = null,
    onArtworkClicked: (UUID) -> Unit
) {
    // Zoom will remain the same even on room change
    val scale = remember(key1 = true) { mutableStateOf(1f) }

    val animX = remember(key1 = room) { Animatable(initialValue = 0f) }

    val animY = remember(key1 = room) { Animatable(initialValue = 0f) }

    val state = rememberTransformableState { zoomChange, _, _ ->
        var newScale = scale.value * zoomChange
        newScale = if (newScale > 1f) 1f else newScale

        scale.value = newScale
    }

    val pictureImageVector = ImageVector.vectorResource(id = R.drawable.picture)
    val picturePainter = rememberVectorPainter(
        defaultWidth = pictureImageVector.defaultWidth,
        defaultHeight = pictureImageVector.defaultHeight,
        viewportWidth = pictureImageVector.viewportWidth,
        viewportHeight = pictureImageVector.viewportHeight,
        name = RootGroupName,
        tintColor = MaterialTheme.colorScheme.primaryContainer,
        tintBlendMode = pictureImageVector.tintBlendMode,
        autoMirror = false,
    ) { _, _ ->
        RenderVectorGroup(group = pictureImageVector.root)
    }

    val sculptureImageVector = ImageVector.vectorResource(id = R.drawable.sculpture)
    val sculpturePainter = rememberVectorPainter(
        defaultWidth = sculptureImageVector.defaultWidth,
        defaultHeight = sculptureImageVector.defaultHeight,
        viewportWidth = sculptureImageVector.viewportWidth,
        viewportHeight = sculptureImageVector.viewportHeight,
        name = RootGroupName,
        tintColor = MaterialTheme.colorScheme.primaryContainer,
        tintBlendMode = sculptureImageVector.tintBlendMode,
        autoMirror = false,
    ) { _, _ ->
        RenderVectorGroup(group = sculptureImageVector.root)
    }

    val userImageVector = ImageVector.vectorResource(id = R.drawable.user)
    val userPainter = rememberVectorPainter(
        defaultWidth = userImageVector.defaultWidth,
        defaultHeight = userImageVector.defaultHeight,
        viewportWidth = userImageVector.viewportWidth,
        viewportHeight = userImageVector.viewportHeight,
        name = RootGroupName,
        tintColor = MaterialTheme.colorScheme.onPrimary,
        tintBlendMode = userImageVector.tintBlendMode,
        autoMirror = false,
    ) { _, _ ->
        RenderVectorGroup(group = userImageVector.root)
    }

    val starredPainter = rememberVectorPainter(image = ImageVector.vectorResource(id = R.drawable.star))
    val visitedPainter = rememberVectorPainter(image = ImageVector.vectorResource(id = R.drawable.visit))

    val artworkPositions = remember { arrayListOf<Triple<UUID, Rect, Offset>>() }

    val hScrollState = rememberScrollState()
    val vScrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .horizontalScroll(hScrollState)
            .verticalScroll(vScrollState)
            .padding(10.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        val art = artworkPositions.find { pos -> pos.second.contains(it) }
                        if (art != null) {
                            onArtworkClicked(art.first)
                        }
                    }
                )
            }
    ) {
        Canvas(
            modifier = Modifier
                .size(1000.dp, 1000.dp)
                .graphicsLayer(
                    scaleX = scale.value,
                    scaleY = scale.value,
                )
                .transformable(state = state)
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

                room.walls.forEach {
                    when (it.first) {
                        PerimeterEntity.LINE -> lineTo(it.second.dp.toPx(), it.third.dp.toPx())
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

            val starredPath = Path().apply {
                room.starredPath.forEach {
                    when (it.first) {
                        PerimeterEntity.LINE -> lineTo(it.second.dp.toPx(), it.third.dp.toPx())
                        PerimeterEntity.MOVE -> moveTo(it.second.dp.toPx(), it.third.dp.toPx())
                    }
                }
            }

            drawPath(
                path = starredPath,
                color = Color.Green.copy(alpha = 0.2f),
                style = Stroke(30.dp.toPx()),
            )

            clipPath(path = groundPath) {}

            artworkPositions.clear()

            // Draw artworks
            room.artworks.forEach {
                val rect = Rect(
                    offset = Offset(x = it.posX.dp.toPx(), y = it.posY.dp.toPx()),
                    size = Size(width = artworkSize.toPx(), artworkSize.toPx())
                )

                val offset = when (it.side) {
                    Side.LEFT -> Offset(
                        x = rect.left - userSize.toPx() - userDistanceFromArtwork.toPx(),
                        y = rect.top + artworkSize.toPx() / 2 - userSize.toPx() / 2
                    )
                    Side.RIGHT -> Offset(
                        x = rect.right + userDistanceFromArtwork.toPx(),
                        y = rect.top + artworkSize.toPx() / 2 - userSize.toPx() / 2
                    )
                    Side.UP -> Offset(
                        x = rect.left + artworkSize.toPx() / 2 - userSize.toPx() / 2,
                        y = rect.top - userSize.toPx() - userDistanceFromArtwork.toPx()
                    )
                    Side.DOWN -> Offset(
                        x = rect.left + artworkSize.toPx() / 2 - userSize.toPx() / 2,
                        y = rect.bottom + userDistanceFromArtwork.toPx()
                    )
                }

                artworkPositions.add(
                    Triple(
                        first = it.beacon,
                        second = rect,
                        third = offset
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
                        draw(
                            size = Size(artworkSize.toPx(), artworkSize.toPx()),
                        )
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


        LaunchedEffect(
            key1 = currentArtwork
        ) {
            if (lastArtwork != null) {
                val offset = artworkPositions.find { it.first == lastArtwork.id }!!.third
                animX.animateTo(
                    targetValue = offset.x,
                    animationSpec = tween(0)
                )

                animY.animateTo(
                    targetValue = offset.y,
                    animationSpec = tween(0)
                )
            }


            if (currentArtwork != null) {
                val offset = artworkPositions.find { it.first == currentArtwork.id }!!.third

                animX.animateTo(
                    targetValue = offset.x,
                    animationSpec = tween(1000)
                )

                animY.animateTo(
                    targetValue = offset.y,
                    animationSpec = tween(1000)
                )
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



