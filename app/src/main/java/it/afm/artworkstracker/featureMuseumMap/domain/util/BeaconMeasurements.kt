package it.afm.artworkstracker.featureMuseumMap.domain.util

import java.util.concurrent.ArrayBlockingQueue

class BeaconMeasurements {
    private val realContainer = ArrayBlockingQueue<Measurement>(size)

    fun push(item: Double) {
        if (realContainer.size >= size) realContainer.remove()
        realContainer.add(Measurement(item, System.currentTimeMillis()))
    }

    fun getMeasures(): List<Double> {
        return realContainer.map { item -> item.measure }.toList()
    }

    fun getLastTimestamp(): Long {
        return realContainer.last().timestamp
    }

    companion object{
        const val size: Int = 5
    }
}