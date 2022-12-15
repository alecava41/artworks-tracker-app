package it.afm.artworkstracker

import android.content.Context
import android.content.pm.PackageManager
import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import it.afm.artworkstracker.ui.theme.ArtworksTrackerTheme
import it.afm.artworkstracker.utils.Measurement
import it.afm.artworkstracker.utils.RestrictedContainer
import kotlinx.coroutines.flow.MutableStateFlow
import org.altbeacon.beacon.*
import org.altbeacon.beacon.service.RunningAverageRssiFilter
import java.net.InetAddress
import java.time.LocalDateTime
import java.util.UUID
import kotlin.concurrent.fixedRateTimer

class MainActivity : ComponentActivity(), RangeNotifier {
    private var ip: InetAddress? = null
    private var port: Int? = null

    // TODO: create an appropriate class to handle this beauty
    private val beaconsMap = mutableMapOf<UUID, Pair<Boolean, RestrictedContainer<Measurement>>>()

    // TODO: (for future implementation) artwork information should be manual (snackbar) + setting to make it auto

    // TODO: make all beacon-aware classes (containers) concurrent

    // TODO: create data-structure to handle already visited beacon

    // TODO: create periodic function to remove out-of-range beacons from the current ones


    override fun didRangeBeaconsInRegion(beacons: Collection<Beacon>, region: Region) {
        for (beacon in beacons) {
            if (beacon.distance < MIN_CONSIDERABLE_DISTANCE) {
                val uuid = beacon.id1.toUuid()

                if (!beaconsMap.containsKey(uuid))
                    beaconsMap[uuid] = Pair(false, RestrictedContainer(MAX_CONSIDERED_MEASUREMENTS))

                beaconsMap[uuid]!!.second.push(Measurement(beacon.distance, LocalDateTime.now()))
            }
        }
    }

    private val locationRequestLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { res ->
        if (res.filter { permission -> !permission.value }.isNotEmpty()) {
            // Permissions not granted
            Toast.makeText(this, "Permissions not granted", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    private lateinit var nsdManager: NsdManager

    private val resolveListener = object: NsdManager.ResolveListener {
        override fun onResolveFailed(service: NsdServiceInfo, error: Int) {
            Log.e("MainActivity", "Cannot resolve service ${service.serviceName}, error = $error")
        }

        override fun onServiceResolved(serviceInfo: NsdServiceInfo) {
            Log.d("MainActivity", "Found service: ip ${serviceInfo.host}, port ${serviceInfo.port}")
            ip = serviceInfo.host
            port = serviceInfo.port
        }
    }

    private val discoveryListener = object : NsdManager.DiscoveryListener {
        // Called as soon as service discovery begins.
        override fun onDiscoveryStarted(regType: String) {
            Log.d("MainActivity", "Service discovery started")
        }

        override fun onServiceFound(service: NsdServiceInfo) {
            // A service was found! Do something with it.
            Log.d("MainActivity", "Service discovery success: $service")

            if (service.serviceName == "Tutorial") {
                // Desired backend service
                nsdManager.stopServiceDiscovery(this)
                nsdManager.resolveService(service, resolveListener)
            }
        }

        override fun onServiceLost(service: NsdServiceInfo) {
            // When the network service is no longer available.
            // Internal bookkeeping code goes here.
            Log.e("MainActivity", "service lost: $service")
        }

        override fun onDiscoveryStopped(serviceType: String) {
            Log.i("MainActivity", "Discovery stopped: $serviceType")
        }

        override fun onStartDiscoveryFailed(serviceType: String, errorCode: Int) {
            Log.e("MainActivity", "Discovery failed: Error code:$errorCode")
        }

        override fun onStopDiscoveryFailed(serviceType: String, errorCode: Int) {
            Log.e("MainActivity", "Discovery failed: Error code:$errorCode")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ArtworksTrackerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CompleteView(this::transmitAsADevice, this::discoverServices)
                }
            }
        }

        nsdManager = getSystemService(Context.NSD_SERVICE) as NsdManager

        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.BLUETOOTH_ADVERTISE
            ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.BLUETOOTH_SCAN
            ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            locationRequestLauncher.launch(arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.BLUETOOTH_ADVERTISE,
                android.Manifest.permission.BLUETOOTH_CONNECT,
                android.Manifest.permission.BLUETOOTH_SCAN
            )
            )
        }
    }

    private fun discoverServices() {
        nsdManager.discoverServices("_http._tcp", NsdManager.PROTOCOL_DNS_SD, discoveryListener)
    }

    override fun onPause() {
        super.onPause()

        nsdManager.stopServiceDiscovery(discoveryListener)
    }

    private fun showBeaconToast(uuid: UUID) {
        runOnUiThread {
            Toast.makeText(this@MainActivity, "You are really close to beacon $uuid", Toast.LENGTH_SHORT).show()
        }
    }

    private fun transmitAsADevice() {
        val beaconManager = BeaconManager.getInstanceForApplication(this)
        val region = Region("all-beacon-region", null, null, null)

        val regionViewModel = beaconManager.getRegionViewModel(region)
//        regionViewModel.rangedBeacons.observe(this, rangingObserver)

        beaconManager.beaconParsers.add(BeaconParser().setBeaconLayout(BeaconParser.ALTBEACON_LAYOUT))
        beaconManager.beaconParsers.add(BeaconParser().setBeaconLayout("m:0-3=4c000215,i:4-19,i:20-21,i:22-23,p:24-24"))

        beaconManager.foregroundScanPeriod = 1100L
        beaconManager.foregroundBetweenScanPeriod = 0L

        BeaconManager.setRssiFilterImplClass(RunningAverageRssiFilter::class.java)
        RunningAverageRssiFilter.setSampleExpirationMilliseconds(5000L)

        beaconManager.addRangeNotifier(this)

        beaconManager.startRangingBeacons(region)

        Toast.makeText(this@MainActivity, "Start ranging", Toast.LENGTH_LONG).show()

        val proximityBeaconDetector = fixedRateTimer("proximityBeaconDetection", false, 6000L, 3000L) {
            for (beaconInRange in beaconsMap) {
                if (!beaconInRange.value.first) {
                    val measurements = beaconInRange.value.second.getAllValues()
                    var sum = 0.0

                    if (measurements.size == MAX_CONSIDERED_MEASUREMENTS) {
                        for (measurement in measurements) sum += measurement.measure
                        val mean = sum / MAX_CONSIDERED_MEASUREMENTS.toDouble()

                        Log.i("MainActivity", "Making mean on beacon ${beaconInRange.key} ($mean) (raw = ${beaconInRange.value.second.getAllValues()}))")

                        if (mean < MIN_BEACON_DISTANCE) {
                            showBeaconToast(beaconInRange.key)
                            beaconsMap[beaconInRange.key] = beaconInRange.value.copy(true, beaconInRange.value.second)
                        }
                    }
                }
            }
        }

        Handler(Looper.myLooper()!!).postDelayed(
            {
                beaconManager.stopRangingBeacons(region)
                Toast.makeText(this@MainActivity, "Ranging terminated", Toast.LENGTH_LONG).show()
                proximityBeaconDetector.cancel()
            },
            60000
        )
    }

    companion object {
        const val MIN_CONSIDERABLE_DISTANCE = 3.0
        const val MAX_CONSIDERED_MEASUREMENTS = 5
        const val MIN_BEACON_DISTANCE = 0.5
    }
}

val beaconOne = MutableStateFlow("")
val beaconTwo = MutableStateFlow("")

@Composable
fun CompleteView(deviceFun: () -> Unit, servicesFun: () -> Unit) {
    Column {
        Buttons(deviceFun = deviceFun, servicesFun = servicesFun)

        val textOne by beaconOne.collectAsState()
        val textTwo by beaconTwo.collectAsState()

        Text(text = textOne)
        Text(text = textTwo)
    }
}

@Composable
fun Buttons(deviceFun: () -> Unit, servicesFun: () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.aligned(Alignment.CenterHorizontally)
    ) {
        ExtendedFloatingActionButton(
            onClick = deviceFun
        ) {
            Text(text = "Device")
            Icon(
                painter = painterResource(id = R.drawable.device),
                contentDescription = "Device icon"
            )
        }

        ExtendedFloatingActionButton(
            onClick = servicesFun
        ) {
            Text(text = "Services")
            Icon(
                painter = painterResource(id = R.drawable.beacon),
                contentDescription = "Services icon"
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ArtworksTrackerTheme {
        CompleteView({}, {})
    }
}