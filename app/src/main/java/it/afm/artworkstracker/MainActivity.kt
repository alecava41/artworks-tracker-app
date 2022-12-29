package it.afm.artworkstracker

import android.content.Context
import android.content.pm.PackageManager
import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import dagger.hilt.android.AndroidEntryPoint
import it.afm.artworkstracker.featureArtwork.domain.model.Artwork
import it.afm.artworkstracker.featureArtwork.presentation.components.ArtworkComponent
import it.afm.artworkstracker.featureMuseumMap.domain.model.ArtworkInfo
import it.afm.artworkstracker.featureMuseumMap.domain.model.Room
import it.afm.artworkstracker.featureMuseumMap.domain.util.ArtworkEnum
import it.afm.artworkstracker.featureMuseumMap.domain.util.PerimeterEnum
import it.afm.artworkstracker.featureMuseumMap.presentation.MuseumMapEvent
import it.afm.artworkstracker.featureMuseumMap.presentation.MuseumMapViewModel
import it.afm.artworkstracker.featureMuseumMap.presentation.components.RoomMap
import it.afm.artworkstracker.ui.theme.ArtworksTrackerTheme
import java.util.*

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MuseumMapViewModel by viewModels()

    // TODO: (future implementation) artwork information should be manual (snackbar) + setting to make it auto

    // TODO (onResume) check if: bluetooth is enabled, wifi is enabled, location is enabled, permissions (?)

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

    private val resolveListener = object : NsdManager.ResolveListener {
        override fun onResolveFailed(service: NsdServiceInfo, error: Int) {
            Log.e("MainActivity", "Cannot resolve service ${service.serviceName}, error = $error")
        }

        override fun onServiceResolved(serviceInfo: NsdServiceInfo) {
            Log.d("MainActivity", "Found service: ip ${serviceInfo.host}, port ${serviceInfo.port}")
            viewModel.onEvent(
                MuseumMapEvent.BackendServerDiscovered(
                    ip = serviceInfo.host.toString(),
                    port = serviceInfo.port.toString()
                )
            )
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

            if (service.serviceName == "MuseumBackend") {
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

    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ArtworksTrackerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    ArtworkComponent(
                        artwork = Artwork(
                            author = "Igor Zawaleski",
                            title = "Your death is coming closer and closer but anyway we will live",
                            description = "La Gioconda ritrae a metà figura una giovane donna con lunghi " +
                                    "capelli scuri. È inquadrata di tre quarti, il busto è rivolto alla " +
                                    "sua destra, il volto verso l'osservatore. Le mani sono incrociate " +
                                    "in primo piano e con le braccia si appoggia a quello che sembra il " +
                                    "bracciolo di una sedia. " +
                                    "La Gioconda, nota anche come Monna Lisa, è un dipinto a olio " +
                                    "su tavola di pioppo realizzato da Leonardo da Vinci, databile " +
                                    "al 1503-1506 circa e conservato nel Museo del Louvre di Parigi. " +
                                    "La Gioconda, nota anche come Monna Lisa, è un dipinto a olio su tavola " +
                                    "di pioppo realizzato da Leonardo da Vinci, databile al " +
                                    "1503-1506 circa e conservato nel Museo del Louvre di Parigi." +
                                    "La Gioconda, nota anche come Monna Lisa, è un dipinto a olio " +
                                    "su tavola di pioppo realizzato da Leonardo da Vinci, databile " +
                                    "al 1503-1506 circa e conservato nel Museo del Louvre di Parigi. " +
                                    "La Gioconda, nota anche come Monna Lisa, è un dipinto a olio su tavola " +
                                    "di pioppo realizzato da Leonardo da Vinci, databile al " +
                                    "1503-1506 circa e conservato nel Museo del Louvre di Parigi.",
                            id = UUID.randomUUID()
                        )
                    )


                }
            }
        }


        nsdManager = getSystemService(Context.NSD_SERVICE) as NsdManager
        nsdManager.discoverServices("_http._tcp", NsdManager.PROTOCOL_DNS_SD, discoveryListener)

        requestPermissions()
    }


    override fun onResume() {
        super.onResume()
        viewModel.onEvent(MuseumMapEvent.ResumeTour)
    }

    override fun onPause() {
        super.onPause()
        viewModel.onEvent(MuseumMapEvent.PauseTour)
    }

    private fun requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.BLUETOOTH_SCAN
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                locationRequestLauncher.launch(
                    arrayOf(
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.BLUETOOTH_SCAN,
                        android.Manifest.permission.BLUETOOTH_CONNECT
                    )
                )
            }
        } else {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.BLUETOOTH
                ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.BLUETOOTH_ADMIN
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                locationRequestLauncher.launch(
                    arrayOf(
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.BLUETOOTH,
                        android.Manifest.permission.BLUETOOTH_ADMIN
                    )
                )
            }
        }
    }
}

val room = Room(
    name = "King's bedroom",
    perimeter = listOf(
        Triple(PerimeterEnum.MOVE, 0, 0),
        Triple(PerimeterEnum.LINE, 0, 125),
        Triple(PerimeterEnum.MOVE, 0, 375),
        Triple(PerimeterEnum.LINE, 0, 500),
        Triple(PerimeterEnum.LINE, 500, 500),
        Triple(PerimeterEnum.LINE, 500, 1000),
        Triple(PerimeterEnum.LINE, 625, 1000),
        Triple(PerimeterEnum.MOVE, 875, 1000),
        Triple(PerimeterEnum.LINE, 1000, 1000),
        Triple(PerimeterEnum.LINE, 1000, 0),
        Triple(PerimeterEnum.LINE, 0, 0)
    ),
    artworks = listOf(
        ArtworkInfo(
            id = 1,
            beacon = UUID.randomUUID(),
            starred = true,
            type = ArtworkEnum.PICTURE,
            posX = 50,
            posY = 50
        ),
        ArtworkInfo(
            id = 2,
            beacon = UUID.randomUUID(),
            starred = true,
            type = ArtworkEnum.SCULPTURE,
            posX = 250,
            posY = 50
        ),
        ArtworkInfo(
            id = 3,
            beacon = UUID.randomUUID(),
            starred = true,
            type = ArtworkEnum.PICTURE,
            posX = 500,
            posY = 50
        )
    ),
    id = 1
)

@Preview(showBackground = true)
@Composable
fun DefaultMuseumMapPreview() {
    ArtworksTrackerTheme {
        Surface {
            RoomMap(room)
        }
    }
}
