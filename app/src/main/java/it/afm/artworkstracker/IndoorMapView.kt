package it.afm.artworkstracker

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.view.View

class IndoorMapView(ctx: Context): View(ctx) {
    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        canvas.maximumBitmapWidth
        canvas.drawLine(10f,10f,12f,12f, Paint())
    }
}