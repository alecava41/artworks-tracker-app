package it.afm.artworkstracker

import android.content.Context
import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import it.afm.artworkstracker.featureArtwork.presentation.components.ArtworkScreen
import it.afm.artworkstracker.featureMuseumMap.presentation.MuseumMapEvent
import it.afm.artworkstracker.featureMuseumMap.presentation.MuseumMapViewModel
import it.afm.artworkstracker.featureMuseumMap.presentation.components.MuseumMapScreen
import it.afm.artworkstracker.ui.theme.ArtworksTrackerTheme
import it.afm.artworkstracker.util.PermissionsUtil
import it.afm.artworkstracker.util.Screen

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val museumMapViewModel: MuseumMapViewModel by viewModels()
    private lateinit var tts: TextToSpeech

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
            museumMapViewModel.onEvent(
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

            if (service.serviceName.contains("MuseumBackend")) {
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

    @OptIn(ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.i("MainActivity", museumMapViewModel.hashCode().toString())

        setContent {
            ArtworksTrackerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier
                        .fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = Screen.MuseumMapScreen.route
                    ) {
//                        dialog(route = Screen.BluetoothPermissionDialog.route) {
//                            BluetoothPermissionDialog(navController = navController)
//                        }

                        composable(route = Screen.MuseumMapScreen.route) {
                            MuseumMapScreen(
                                navController = navController,
                                viewModel = museumMapViewModel
                            )
                        }

                        dialog(
                            route = Screen.ArtworkScreen.route + "?artId={artId}&url={url}",
                            dialogProperties = DialogProperties(
                                usePlatformDefaultWidth = false
                            ),
                            arguments = listOf(
                                navArgument(
                                    name = "artId"
                                ) {
                                    type = NavType.StringType
                                    defaultValue = ""
                                },
                                navArgument(
                                    name = "url"
                                ) {
                                    type = NavType.StringType
                                    defaultValue = ""
                                }
                            )
                        ) {
                            ArtworkScreen(
                                navController = navController,
                                viewModel = hiltViewModel(),
                                tts = tts
                            )
                        }
                    }
                }
            }
        }

        nsdManager = getSystemService(Context.NSD_SERVICE) as NsdManager
        nsdManager.discoverServices("_http._tcp", NsdManager.PROTOCOL_DNS_SD, discoveryListener)


        // TODO: check whether audio is > 0?
        tts = TextToSpeech(this) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val locale = this.resources.configuration.locales[0]
                tts.language = locale
            } else {
                Toast.makeText(this, "Initialization failed!", Toast.LENGTH_SHORT).show()
                // TODO: disable related commands
            }
        }

        if (!PermissionsUtil.checkPermissions(this))
            PermissionsUtil.requestPermissions(locationRequestLauncher)
    }


    override fun onDestroy() {
        super.onDestroy()
        tts.shutdown()
    }

    override fun onResume() {
        super.onResume()
        museumMapViewModel.onEvent(MuseumMapEvent.ResumeTour)
    }

    override fun onPause() {
        super.onPause()
        museumMapViewModel.onEvent(MuseumMapEvent.PauseTour)
    }
}
