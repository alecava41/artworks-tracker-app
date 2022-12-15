package it.afm.artworkstracker

import org.altbeacon.beacon.service.RssiFilter

class CustomRssiFilter: RssiFilter {
    private var rssi: Int = 0

    override fun addMeasurement(rssi: Int?) {
        if (rssi != null && rssi != 0)
            this.rssi = rssi
    }

    override fun noMeasurementsAvailable(): Boolean {
        return rssi == 0
    }

    override fun calculateRssi(): Double {
        return rssi.toDouble()
    }

    override fun getMeasurementCount(): Int {
        return 1
    }
}