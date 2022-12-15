package it.afm.artworkstracker

import android.bluetooth.le.AdvertiseCallback
import android.bluetooth.le.AdvertiseSettings
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import it.afm.artworkstracker.ui.theme.ArtworksTrackerTheme
import kotlinx.coroutines.flow.MutableStateFlow
import org.altbeacon.beacon.*
import org.altbeacon.beacon.service.ArmaRssiFilter
import org.altbeacon.beacon.service.RunningAverageRssiFilter
import java.math.BigDecimal
import java.math.RoundingMode
import java.net.InetAddress


class MainActivity : ComponentActivity(), RangeNotifier {
    private var ip: InetAddress? = null
    private var port: Int? = null


    private val locationRequestLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { res ->
        if (res.filter { permission -> !permission.value }.isNotEmpty()) {
            // Permissions not granted
            Toast.makeText(this, "Permissions not granted", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    private val rangingObserver = Observer<Collection<Beacon>> { beacons ->
        var newBeacons = ""

        for (beacon in beacons) {
            val distance = BigDecimal(beacon.distance).setScale(2, RoundingMode.CEILING)
            Log.i("MainActivity", "${beacon.manufacturer} - ${beacon.distance}m")
            val beaconStr = "${beacon.manufacturer} - ${distance}m\n"
            newBeacons += beaconStr
        }

//        if (newBeacons.isNotEmpty())
//            beaconsMap.value = beaconsMap.value + newBeacons
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
                    CompleteView(this::transmitAsABeacon, this::transmitAsADevice, this::discoverServices)
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

    private fun transmitAsABeacon() {
        val beacon = Beacon.Builder()
            .setIdentifiers(mutableListOf(
                Identifier.fromInt(1),
                Identifier.fromInt(2),
                Identifier.fromInt(3)
            ))
            .setDataFields(mutableListOf(10L))
            .setTxPower(-59)
            .setManufacturer(2)
            .build()

        val beaconParser = BeaconParser().setBeaconLayout(BeaconParser.ALTBEACON_LAYOUT)

        val beaconTransmitter = BeaconTransmitter(applicationContext, beaconParser)

        beaconTransmitter.startAdvertising(beacon, object : AdvertiseCallback() {
            override fun onStartSuccess(settingsInEffect: AdvertiseSettings?) {
                Toast.makeText(
                    this@MainActivity,
                    "Advertising started successfully",
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun onStartFailure(errorCode: Int) {
                Toast.makeText(this@MainActivity, "Advertising error = $errorCode", Toast.LENGTH_LONG).show()
            }
        })

        Handler(Looper.myLooper()!!).postDelayed(
            {
                beaconTransmitter.stopAdvertising()
                Toast.makeText(this@MainActivity, "Advertising terminated", Toast.LENGTH_LONG).show()
            },
            60000
        )
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

//        BeaconManager.setRssiFilterImplClass(ArmaRssiFilter::class.java)

        beaconManager.addRangeNotifier(this)

        beaconManager.startRangingBeacons(region)

        Toast.makeText(this@MainActivity, "Start ranging", Toast.LENGTH_LONG).show()

        Handler(Looper.myLooper()!!).postDelayed(
            {
                beaconManager.stopRangingBeacons(region)
                Toast.makeText(this@MainActivity, "Ranging terminated", Toast.LENGTH_LONG).show()
            },
            60000
        )
    }

    override fun didRangeBeaconsInRegion(beacons: Collection<Beacon>, region: Region) {
        val beacon2 = beacons.find { beacon -> beacon.id1.toString().startsWith("efdffba") }
        val beacon1 = beacons.find { beacon -> !beacon.id1.toString().startsWith("efdffba") }

        if (beacon2 != null) {
            if (beacon2.distance < 3.0)
                beaconTwo.value = "02 - ${beacon2.distance}m"
            else
                beaconTwo.value = "02 - > 3m"
        }

        if (beacon1 != null) {
            if (beacon1.distance < 3.0)
                beaconOne.value = "76 - ${beacon1.distance}m"
            else
                beaconOne.value = "76 - > 3m"
        }
    }
}

val beaconOne = MutableStateFlow("")
val beaconTwo = MutableStateFlow("")

@Composable
fun CompleteView(beaconFun: () -> Unit, deviceFun: () -> Unit, servicesFun: () -> Unit) {
    Column {
        Buttons(beaconFun = beaconFun, deviceFun = deviceFun, servicesFun = servicesFun)

        val textOne by beaconOne.collectAsState()
        val textTwo by beaconTwo.collectAsState()

        Text(text = textOne)
        Text(text = textTwo)
    }
}

@Composable
fun Buttons(beaconFun: () -> Unit, deviceFun: () -> Unit, servicesFun: () -> Unit) {
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
            onClick = beaconFun
        ) {
            Text(text = "Beacon")
            Icon(
                painter = painterResource(id = R.drawable.beacon),
                contentDescription = "Beacon icon"
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
        CompleteView({}, {}, {})
    }
}