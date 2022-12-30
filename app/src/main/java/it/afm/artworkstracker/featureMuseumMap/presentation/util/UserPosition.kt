package it.afm.artworkstracker.featureMuseumMap.presentation.util

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.unit.Density
import it.afm.artworkstracker.featureMuseumMap.domain.util.Side
import it.afm.artworkstracker.featureMuseumMap.presentation.components.artworkSize
import it.afm.artworkstracker.featureMuseumMap.presentation.components.userDistanceFromArtwork
import it.afm.artworkstracker.featureMuseumMap.presentation.components.userSize

object UserPosition {
    fun calculatePositionBasedOnArtworkPosition(position: Rect, side: Side, density: Float): Offset {
        return when(side) {
            Side.LEFT -> Offset(
                x = position.left - (userSize.value * density) - (userDistanceFromArtwork.value * density),
                y = position.top + (artworkSize.value * density) / 2 - (userSize.value * density) / 2
            )
            Side.RIGHT -> Offset(
                x = position.right + (userDistanceFromArtwork.value * density),
                y = position.top + (artworkSize.value * density) / 2 - (userSize.value * density) / 2
            )
            Side.UP -> Offset(
                x = position.left + (artworkSize.value * density) / 2 - (userSize.value * density) / 2,
                y = position.top - (userSize.value * density) - (userDistanceFromArtwork.value * density)
            )
            Side.DOWN -> Offset(
                x = position.left + (artworkSize.value * density) / 2 - (userSize.value * density) / 2,
                y = position.bottom + (userDistanceFromArtwork.value * density)
            )
        }
    }
}