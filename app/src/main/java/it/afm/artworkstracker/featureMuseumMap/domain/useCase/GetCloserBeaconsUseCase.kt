package it.afm.artworkstracker.featureMuseumMap.domain.useCase

import it.afm.artworkstracker.featureMuseumMap.domain.model.Beacon
import it.afm.artworkstracker.featureMuseumMap.domain.repository.BeaconsRepository
import it.afm.artworkstracker.featureMuseumMap.domain.util.BeaconMeasurements
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.onEach
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.concurrent.fixedRateTimer

class GetCloserBeaconsUseCase(
    private val repository: BeaconsRepository
) {
    private val closestBeacon = MutableSharedFlow<Beacon>()
    private val closerBeaconsMap = ConcurrentHashMap<Beacon, BeaconMeasurements>()
    private val closerBeaconFlow = repository.getCloserBeacons()

    private var proximityBeaconDetector: Timer? = null
    private var beaconsCleaner: Timer? = null

    init {
        closerBeaconFlow.onEach { beacons ->
            for (beacon in beacons) {
                if (!closerBeaconsMap.containsKey(beacon))
                    closerBeaconsMap[beacon] = BeaconMeasurements()

                closerBeaconsMap[beacon]!!.push(beacon.distance)
            }
        }
    }

    operator fun invoke(): Flow<Beacon> = closestBeacon

    fun startListeningForBeacons() {
        repository.startListeningForBeacons()

        proximityBeaconDetector = restartProximityDetection()
        beaconsCleaner = restartBeaconsCleaning()
    }

    fun stopListeningForBeacons() {
        repository.stopListeningForBeacons()

        proximityBeaconDetector?.cancel()
        beaconsCleaner?.cancel()
    }

    private fun restartBeaconsCleaning(): Timer {
        return fixedRateTimer(
            name = "beaconCleaner",
            daemon = false,
            initialDelay = BEACONS_CLEANER_INITIAL_DELAY,
            period = BEACONS_CLEANER_PERIODICITY
        ) {
            closerBeaconsMap.forEach {
                val elapsedSeconds =
                    (System.currentTimeMillis() - it.value.getLastTimestamp()) / 1000

                if (elapsedSeconds > MAX_BEACON_LIFE_IN_RANGE)
                    closestBeacon.tryEmit(it.key)
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

                if (mean < MIN_BEACON_DISTANCE)
                    closestBeacon.tryEmit(it.key)
            }
        }
    }

    companion object {
        const val MIN_BEACON_DISTANCE = 0.5
        const val MAX_BEACON_LIFE_IN_RANGE = 10

        const val PROXIMITY_DETECTION_INITIAL_DELAY = 6000L
        const val PROXIMITY_DETECTION_PERIODICITY = 5000L

        const val BEACONS_CLEANER_INITIAL_DELAY = 10000L
        const val BEACONS_CLEANER_PERIODICITY = 5000L
    }
}