package it.afm.artworkstracker.featureMuseumMap.data.dataSource.beacon

import android.content.Context
import android.util.Log
import it.afm.artworkstracker.featureMuseumMap.domain.model.Beacon
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.altbeacon.beacon.BeaconManager
import org.altbeacon.beacon.BeaconParser
import org.altbeacon.beacon.RangeNotifier
import org.altbeacon.beacon.Region
import org.altbeacon.beacon.service.RunningAverageRssiFilter
import java.util.concurrent.ConcurrentLinkedQueue

class BeaconDataSourceImpl(ctx: Context) : BeaconsDataSource, RangeNotifier {
    private val beaconsInRange: ConcurrentLinkedQueue<Beacon> = ConcurrentLinkedQueue()

    private var isScanning = false

    private val beaconManager = BeaconManager.getInstanceForApplication(ctx)
    private val region = Region("all-beacon-region", null, null, null)

    init {
        setupBeaconListener()
    }

    private fun setupBeaconListener() {
        beaconManager.beaconParsers.add(BeaconParser().setBeaconLayout(BeaconParser.ALTBEACON_LAYOUT))
        beaconManager.beaconParsers.add(BeaconParser().setBeaconLayout(IBEACON_LAYOUT))

        BeaconManager.setRssiFilterImplClass(RunningAverageRssiFilter::class.java)
        RunningAverageRssiFilter.setSampleExpirationMilliseconds(5000L)

        beaconManager.addRangeNotifier(this)
    }

    override fun getCloserBeacons(): Flow<List<Beacon>> = flow {
        while (true) {
            if (beaconsInRange.isNotEmpty()) {
                if (isScanning)
                    emit(beaconsInRange.toList())
                else
                    beaconsInRange.clear()

                beaconsInRange.forEach {
                    Log.i(TAG, "Ranging beacon ${it.id}, distance = ${it.distance}")
                }
            }

            delay(1200L)
        }
    }

    override fun startListeningForBeacons() {
        beaconManager.startRangingBeacons(region)
        isScanning = true
    }

    override fun stopListeningForBeacons() {
        beaconManager.stopRangingBeacons(region)
        isScanning = false
    }

    override fun didRangeBeaconsInRegion(
        beacons: Collection<org.altbeacon.beacon.Beacon>,
        region: Region
    ) {
        val closerBeacons = beacons.filter {
            it.distance <= MIN_CONSIDERABLE_DISTANCE
        }.map {
            Beacon(it.id1.toUuid(), it.distance)
        }

        beaconsInRange.clear()
        beaconsInRange.addAll(closerBeacons)
    }

    companion object {
        const val TAG = "BeaconDataSourceImpl"
        const val IBEACON_LAYOUT = "m:0-3=4c000215,i:4-19,i:20-21,i:22-23,p:24-24"
        const val MIN_CONSIDERABLE_DISTANCE = 3.0
    }
}