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
    private val closestBeacon = AtomicReference<Beacon>()
    private val closerBeaconsMap = ConcurrentHashMap<Beacon, BeaconMeasurements>()

    private val closerBeaconsFlow = repository.getCloserBeacons().onEach { beacons ->
        Log.i(TAG, beacons.toString())

        for (beacon in beacons) {
            if (!closerBeaconsMap.containsKey(beacon))
                closerBeaconsMap[beacon] = BeaconMeasurements()

            closerBeaconsMap[beacon]!!.push(beacon.distance)
        }
    }.map {
        closestBeacon.get()
    }

    operator fun invoke(): Flow<Beacon> = closerBeaconsFlow

    private var proximityBeaconDetector: Timer? = null
    private var beaconsCleaner: Timer? = null

    fun startListeningForBeacons() {
        repository.startListeningForBeacons()

        proximityBeaconDetector = restartProximityDetection()
        beaconsCleaner = restartBeaconsCleaning()

        Log.i(TAG, "Start listening for beacons!")
    }

    fun stopListeningForBeacons() {
        repository.stopListeningForBeacons()

        proximityBeaconDetector?.cancel()
        beaconsCleaner?.cancel()

        Log.i(TAG, "Stop listening for beacons!")
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

                if (elapsedSeconds > MAX_BEACON_LIFE_IN_RANGE)
                    closerBeaconsMap.remove(it.key)
            }
        }
    }

    private fun restartProximityDetection(): Timer {
        return fixedRateTimer(
            name = "proximityBeaconDetection",
            daemon = false,
            initialDelay = PROXIMITY_DETECTION_INITIAL_DELAY,
            period = PROXIMITY_DETECTION_PERIODICITY
        ) {
            closerBeaconsMap.forEach {
                val measures = it.value.getMeasures()
                val mean = measures.sum() / measures.size

                Log.i(TAG, "Beacon ${it.key}, mean = $mean")

                if (mean < MIN_BEACON_DISTANCE)
                    closestBeacon.set(it.key)
            }
        }
    }

    companion object {
        const val TAG = "GetCloserBeaconsUseCase"

        const val MIN_BEACON_DISTANCE = 0.5
        const val MAX_BEACON_LIFE_IN_RANGE = 10

        const val PROXIMITY_DETECTION_INITIAL_DELAY = 6000L
        const val PROXIMITY_DETECTION_PERIODICITY = 5000L

        const val BEACONS_CLEANER_INITIAL_DELAY = 10000L
        const val BEACONS_CLEANER_PERIODICITY = 5000L
    }
}