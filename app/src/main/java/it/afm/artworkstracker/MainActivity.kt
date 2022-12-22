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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import dagger.hilt.android.AndroidEntryPoint
import it.afm.artworkstracker.ui.theme.ArtworksTrackerTheme
import it.afm.artworkstracker.featureMuseumMap.domain.util.BeaconMeasurements
import kotlinx.coroutines.flow.MutableStateFlow
import org.altbeacon.beacon.*
import org.altbeacon.beacon.service.RunningAverageRssiFilter
import java.net.InetAddress
import java.time.LocalDateTime
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.concurrent.fixedRateTimer

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private var ip: InetAddress? = null
    private var port: Int? = null

    // TODO: (for future implementation) artwork information should be manual (snackbar) + setting to make it auto


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
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ArtworksTrackerTheme {
    }
}