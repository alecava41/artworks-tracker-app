package it.afm.artworkstracker.featureMuseumMap.domain.util

import java.util.concurrent.ArrayBlockingQueue

class BeaconMeasurements {
    private val realContainer = ArrayBlockingQueue<Measurement>(MAX_CONTAINER_SIZE)

    fun push(item: Double) {
        if (realContainer.size >= MAX_CONTAINER_SIZE) realContainer.remove()
        realContainer.add(Measurement(item, System.currentTimeMillis()))
    }

    fun getMean(): Double {
        return if (realContainer.size < MAX_CONTAINER_SIZE) Double.MAX_VALUE
                else realContainer.sumOf { it.measure } / realContainer.size
    }

    fun getLastTimestamp(): Long {
        return realContainer.last().timestamp
    }

    companion object {
        const val MAX_CONTAINER_SIZE: Int = 5
    }
}