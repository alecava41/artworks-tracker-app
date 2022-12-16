package it.afm.artworkstracker.utils

import java.time.LocalDateTime
import java.util.concurrent.ArrayBlockingQueue

class BeaconMeasurementContainer {
    private val realContainer = ArrayBlockingQueue<Measurement>(size)

    fun push(item: Double) {
        if (realContainer.size >= size) realContainer.remove()
        realContainer.add(Measurement(item, LocalDateTime.now()))
    }

    fun getAllValues(): List<Double> {
        return realContainer.map { item -> item.measure }.toList()
    }

    fun getLastTimeStamp(): LocalDateTime {
        return realContainer.last().timestamp
    }

    companion object{
        const val size: Int = 5
    }
}