package it.afm.artworkstracker.featureMuseumMap.data.dataSource

import android.content.Context
import android.util.Log
import it.afm.artworkstracker.featureMuseumMap.domain.model.Beacon
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import org.altbeacon.beacon.BeaconManager
import org.altbeacon.beacon.BeaconParser
import org.altbeacon.beacon.RangeNotifier
import org.altbeacon.beacon.Region
import org.altbeacon.beacon.service.RunningAverageRssiFilter

class BeaconDataSourceImpl(ctx: Context): BeaconsDataSource, RangeNotifier {
    private val beaconsInRangeFlow = MutableSharedFlow<List<Beacon>>()

    private val beaconManager = BeaconManager.getInstanceForApplication(ctx)
    private val region = Region("all-beacon-region", null, null, null)

    init {
        setupBeaconListener()
    }

    private fun setupBeaconListener() {
        beaconManager.beaconParsers.add(BeaconParser().setBeaconLayout(BeaconParser.ALTBEACON_LAYOUT))
        beaconManager.beaconParsers.add(BeaconParser().setBeaconLayout(IBEACON_LAYOUT))

        beaconManager.foregroundScanPeriod = 1100L
        beaconManager.foregroundBetweenScanPeriod = 0L

        BeaconManager.setRssiFilterImplClass(RunningAverageRssiFilter::class.java)
        RunningAverageRssiFilter.setSampleExpirationMilliseconds(5000L)

        beaconManager.addRangeNotifier(this)
    }

    override fun getCloserBeacons(): Flow<List<Beacon>> = beaconsInRangeFlow

    override fun startListeningForBeacons() {
        beaconManager.startRangingBeacons(region)
    }

    override fun stopListeningForBeacons() {
       beaconManager.stopRangingBeacons(region)
    }

    override fun didRangeBeaconsInRegion(
        beacons: MutableCollection<org.altbeacon.beacon.Beacon>?,
        region: Region?
    ) {
        val closerBeacons = beacons?.filter {
            it.distance <= MIN_CONSIDERABLE_DISTANCE
        }?.map {
            Beacon(it.id1.toUuid(), it.distance)
        }

        closerBeacons?.forEach {
            Log.i(TAG, "Beacon ${it.id}, distance = ${it.distance}m")
        }

        if (!closerBeacons.isNullOrEmpty()) {
            val res = beaconsInRangeFlow.tryEmit(closerBeacons)
            Log.i(TAG, "Emitted values = $res, subscribers = ${beaconsInRangeFlow.subscriptionCount.value}")

        } else
            beaconsInRangeFlow.tryEmit(emptyList())
    }

    companion object {
        const val TAG = "BeaconDataSourceImpl"
        const val IBEACON_LAYOUT = "m:0-3=4c000215,i:4-19,i:20-21,i:22-23,p:24-24"
        const val MIN_CONSIDERABLE_DISTANCE = 3.0
    }
}