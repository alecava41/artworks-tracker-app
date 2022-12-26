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
    private val closerBeaconsMap = ConcurrentHashMap<UUID, BeaconMeasurements>()

    private val closerBeaconsFlow = repository.getCloserBeacons().onEach { beacons ->
        Log.i(TAG, beacons.toString())

        beacons.forEach { beacon ->
            if (!closerBeaconsMap.containsKey(beacon.id))
                closerBeaconsMap[beacon.id] = BeaconMeasurements()

            closerBeaconsMap[beacon.id]!!.push(beacon.distance)
        }

        // TODO: decide whether to do it every time new data arrives or every X seconds
        closerBeaconsMap.forEach {
            val measures = it.value.getMeasures()
            val mean = measures.sum() / measures.size

            Log.i(TAG, "Proximity beacon: ${it.key}, mean = $mean")

            if (mean < MIN_BEACON_DISTANCE)
                closestBeacon.set(Beacon(it.key, mean))
        }
    }.map {
        closestBeacon.get()
    }

    operator fun invoke(): Flow<Beacon> = closerBeaconsFlow

//    private var proximityBeaconDetector: Timer? = null
    private var beaconsCleaner: Timer? = null

    fun startListeningForBeacons() {
        repository.startListeningForBeacons()

//        proximityBeaconDetector = restartProximityDetection()
        beaconsCleaner = restartBeaconsCleaning()

        Log.i(TAG, "Start listening for beacons!")
    }

    fun stopListeningForBeacons() {
        repository.stopListeningForBeacons()

//        proximityBeaconDetector?.cancel()
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

                if (elapsedSeconds > MAX_BEACON_LIFE_IN_RANGE) {
                    Log.i(TAG, "Cleaning beacon: ${it.key}, elapsed time = $elapsedSeconds")
                    closerBeaconsMap.remove(it.key)
                }
            }
        }
    }

//    private fun restartProximityDetection(): Timer {
//        return fixedRateTimer(
//            name = "proximityBeaconDetection",
//            daemon = false,
//            initialDelay = PROXIMITY_DETECTION_INITIAL_DELAY,
//            period = PROXIMITY_DETECTION_PERIODICITY
//        ) {
//            closerBeaconsMap.forEach {
//                val measures = it.value.getMeasures()
//                val mean = measures.sum() / measures.size
//
//                Log.i(TAG, "Proximity beacon: ${it.key}, mean = $mean")
//
//                if (mean < MIN_BEACON_DISTANCE)
//                    closestBeacon.set(Beacon(it.key, mean))
//            }
//        }
//    }

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