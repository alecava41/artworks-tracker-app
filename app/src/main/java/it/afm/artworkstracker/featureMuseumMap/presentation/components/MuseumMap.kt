package it.afm.artworkstracker.featureMuseumMap.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun RoomMap() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .horizontalScroll(rememberScrollState())
            .verticalScroll(rememberScrollState())
            .padding(0.dp)

    ) {
        Canvas(
            modifier = Modifier.size(500.dp, 500.dp)
                .align(Alignment.Center)

        ) {
            // Set the color and stroke width for the lines
            val paint = Paint().apply {
                color = Color.Black
                strokeWidth = 10f
            }

            // Draw the outline of the room
            val path = Path().apply {
                moveTo(0.dp.toPx(),0.dp.toPx())
                lineTo(0.dp.toPx(), 250.dp.toPx())
                lineTo(250.dp.toPx(), 250.dp.toPx())
                lineTo(250.dp.toPx(), 500.dp.toPx())
                lineTo(500.dp.toPx(), 500.dp.toPx())
                lineTo(500.dp.toPx(), 0.dp.toPx())
                lineTo(0.dp.toPx(), 0.dp.toPx())
            }

            drawPath(
                path = path,
                color = paint.color,
                style = Stroke(10f)
            )

            // Draw the walls of the room
            drawLine(paint.color, Offset(x = 50f, y = 50f), Offset(x = 50f, y = 150f))
            drawLine(paint.color, Offset(x = 50f, y = 150f), Offset(x = 150f, y = 150f))
            drawLine(paint.color, Offset(x = 150f, y = 150f), Offset(x = 150f, y = 50f))
            drawLine(paint.color, Offset(x = 150f, y = 50f), Offset(x = 50f, y = 50f))

            // Draw a table and chairs
            drawCircle(color = paint.color, center = Offset(x = 100f, y = 100f), radius = 25f)
            drawCircle(color = paint.color, center = Offset(x = 75f, y = 75f), radius = 15f)
            drawCircle(color = paint.color, center = Offset(x = 125f, y = 75f), radius = 15f)
        }
    }
}



