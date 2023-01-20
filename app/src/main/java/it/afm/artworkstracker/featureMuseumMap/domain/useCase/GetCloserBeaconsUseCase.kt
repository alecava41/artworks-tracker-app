package it.afm.artworkstracker.featureMuseumMap.domain.useCase

import android.util.Log
import it.afm.artworkstracker.featureMuseumMap.domain.model.Beacon
import it.afm.artworkstracker.featureMuseumMap.domain.repository.BeaconsRepository
import it.afm.artworkstracker.featureMuseumMap.domain.util.BeaconMeasurements
import kotlinx.coroutines.flow.*
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicReference
import kotlin.concurrent.fixedRateTimer

class GetCloserBeaconsUseCase(
    private val repository: BeaconsRepository
) {
    private val closestBeacon = AtomicReference<Beacon?>(null)
    private val closerBeaconsMap = ConcurrentHashMap<UUID, BeaconMeasurements>()

    private val closerBeaconsFlow = repository.getCloserBeacons().onEach { beacons ->
        beacons.forEach { beacon ->
            if (!closerBeaconsMap.containsKey(beacon.id))
                closerBeaconsMap[beacon.id] = BeaconMeasurements()

            closerBeaconsMap[beacon.id]!!.push(beacon.distance)
        }

        val entry = closerBeaconsMap.minByOrNull { entry ->
            entry.value.getMean()
        }

        if(entry != null) {
            val mean = entry.value.getMean()

            if (mean < MIN_BEACON_DISTANCE) {
                Log.i(TAG, "MIN_BEACON_DISTANCE ${entry.key}: distance = $mean")
                closestBeacon.set(Beacon(entry.key, mean))
            }
        }
    }.map {
        closestBeacon.get()
    }

    operator fun invoke(): Flow<Beacon?> = closerBeaconsFlow

    private var beaconsCleaner: Timer? = null

    fun startListeningForBeacons() {
        Log.i(TAG, "Start listening for beacons!")

        repository.startListeningForBeacons()
        beaconsCleaner = restartBeaconsCleaning()
    }

    fun stopListeningForBeacons() {
        Log.i(TAG, "Stop listening for beacons!")

        repository.stopListeningForBeacons()

        beaconsCleaner?.cancel()
        closestBeacon.set(null)
        closerBeaconsMap.clear()
    }

    private fun restartBeaconsCleaning(): Timer {
        return fixedRateTimer(
            name = "beaconCleaner",
            daemon = false,
            initialDelay = BEACONS_CLEANER_INITIAL_DELAY,
            period = BEACONS_CLEANER_PERIODICITY
        ) {
            closerBeaconsMap.forEach {
                val elapsedSeconds = (System.currentTimeMillis() - it.value.getLastTimestamp()) / 1000

                if (elapsedSeconds > MAX_BEACON_LIFE_IN_RANGE) {
                    Log.i(TAG, "Cleaning beacon: ${it.key}, elapsed time = $elapsedSeconds")
                    closerBeaconsMap.remove(it.key)
                }
            }
        }
    }

    companion object {
        const val TAG = "GetCloserBeaconsUseCase"

        const val MIN_BEACON_DISTANCE = 0.75
        const val MAX_BEACON_LIFE_IN_RANGE = 10

        const val BEACONS_CLEANER_INITIAL_DELAY = 10000L
        const val BEACONS_CLEANER_PERIODICITY = 5000L
    }
}