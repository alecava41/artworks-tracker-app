package it.afm.artworkstracker.featureMuseumMap.presentation.components

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import it.afm.artworkstracker.R
import it.afm.artworkstracker.featureMuseumMap.domain.model.Room
import it.afm.artworkstracker.featureMuseumMap.domain.util.ArtworkEnum
import it.afm.artworkstracker.featureMuseumMap.domain.util.PerimeterEnum

@Composable
fun RoomMap(
    room: Room
) {
    val coroutineScope = rememberCoroutineScope()

    val picturePainter =
        rememberVectorPainter(image = ImageVector.vectorResource(id = R.drawable.picture))
    val visitedPicturePainter =
        rememberVectorPainter(image = ImageVector.vectorResource(id = R.drawable.picture_visited))

    val sculpturePainter =
        rememberVectorPainter(image = ImageVector.vectorResource(id = R.drawable.sculpture))
    val visitedSculpturePainter =
        rememberVectorPainter(image = ImageVector.vectorResource(id = R.drawable.sculpture_visited))

    val userPainter = rememberVectorPainter(
        image = ImageVector.vectorResource(id = R.drawable.user),
    )

    val artworkPositions = arrayListOf<Pair<Int, Rect>>()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .horizontalScroll(rememberScrollState())
            .verticalScroll(rememberScrollState())
            .padding(20.dp)

    ) {
        Canvas(
            modifier = Modifier
                .size(1000.dp, 1000.dp)
                .align(Alignment.Center)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(bounded = true)
                ) {}
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            val id = artworkPositions.find { pos -> pos.second.contains(it) }?.first
                            if (id != null) Log.i("MuseumMap", "$id clicked")
                        }
                    )

                    forEachGesture {
                        awaitPointerEventScope {

                        }
                    }
                }
        ) {

            // Draw the outline of the room
            val path = Path().apply {
                room.perimeter.forEach {
                    when (it.first) {
                        PerimeterEnum.LINE -> lineTo(it.second.dp.toPx(), it.third.dp.toPx())
                        PerimeterEnum.MOVE -> moveTo(it.second.dp.toPx(), it.third.dp.toPx())
                    }
                }
            }

            drawPath(
                path = path,
                color = Color.Black,
                style = Stroke(10.dp.toPx())
            )

//            // Draw artworks and other stuff
            room.artworks.forEach {
                artworkPositions.add(
                    Pair(
                        it.id,
                        Rect(
                            offset = Offset(x = it.posX.dp.toPx(), y = it.posY.dp.toPx()),
                            size = Size(width = 50.dp.toPx(), 50.dp.toPx())
                        )
                    )
                )

                when (it.type) {
                    ArtworkEnum.PICTURE -> {
                        // TODO: add logic to handle already visited artwork

                        translate(
                            left = it.posX.dp.toPx(),
                            top = it.posY.dp.toPx()
                        ) {
                            with(picturePainter) {
                                draw(Size(50.dp.toPx(), 50.dp.toPx()))
                            }
                        }
                    }
                    ArtworkEnum.SCULPTURE -> {
                        // TODO: add logic to handle already visited artwork

                        translate(
                            left = it.posX.dp.toPx(),
                            top = it.posY.dp.toPx()
                        ) {
                            with(sculpturePainter) {
                                draw(Size(50.dp.toPx(), 50.dp.toPx()))
                            }
                        }
                    }
                }
            }

        }
    }
}



