package it.afm.artworkstracker

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
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
import it.afm.artworkstracker.core.presentation.components.BottomBar
import it.afm.artworkstracker.core.presentation.components.TopBar
import it.afm.artworkstracker.featureArtwork.presentation.components.ArtworkScreen
import it.afm.artworkstracker.featureMuseumMap.presentation.MuseumMapEvent
import it.afm.artworkstracker.featureMuseumMap.presentation.MuseumMapViewModel
import it.afm.artworkstracker.featureMuseumMap.presentation.components.MuseumMapScreen
import it.afm.artworkstracker.featureVisitedArtworksList.presentation.components.VisitedArtworksList
import it.afm.artworkstracker.ui.theme.ArtworksTrackerTheme
import it.afm.artworkstracker.util.LanguageUtil
import it.afm.artworkstracker.util.Screen
import java.util.*

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val museumMapViewModel: MuseumMapViewModel by viewModels()
    private var tts: TextToSpeech? = null

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
        override fun onDiscoveryStarted(regType: String) {
            Log.d("MainActivity", "Service discovery started")
        }

        override fun onServiceFound(service: NsdServiceInfo) {
            Log.d("MainActivity", "Service discovery success: $service")

            if (service.serviceName.contains("MuseumBackend")) {
                nsdManager.stopServiceDiscovery(this)
                nsdManager.resolveService(service, resolveListener)
            }
        }

        override fun onServiceLost(service: NsdServiceInfo) {
            Log.e("MainActivity", "service lost: $service")

            if (service.serviceName.contains("MuseumBackend")) {
                museumMapViewModel.onEvent(MuseumMapEvent.BackendServerLost)
            }
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

    private lateinit var connectivityManager: ConnectivityManager
    private val networkRequest = NetworkRequest.Builder()
        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        .build()

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            nsdManager.discoverServices("_http._tcp", NsdManager.PROTOCOL_DNS_SD, discoveryListener)
        }

        override fun onUnavailable() {
            museumMapViewModel.onEvent(MuseumMapEvent.WifiConnectionNotAvailable)
        }

        override fun onLost(network: Network) {
            museumMapViewModel.onEvent(MuseumMapEvent.WifiConnectionNotAvailable)
        }
    }

    private lateinit var bluetoothManager: BluetoothManager
    private val bluetoothUpdateReceiver = object : BroadcastReceiver() {
        // No need to perform check on intent -> it will be always BluetoothAdapter.ACTION_STATE_CHANGED
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)) {
                BluetoothAdapter.STATE_ON -> museumMapViewModel.onEvent(MuseumMapEvent.BluetoothAvailable)
                BluetoothAdapter.STATE_OFF -> museumMapViewModel.onEvent(MuseumMapEvent.BluetoothNotAvailable)
            }
        }
    }

    private val bluetoothEnablerLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {}

    private lateinit var locationManager: LocationManager
    private val locationUpdateReceiver = object : BroadcastReceiver() {
        // No need to perform check on intent -> it will be always LocationManager.MODE_CHANGED_ACTION
        override fun onReceive(context: Context, intent: Intent) {
            val isEnabled =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
                    intent.getBooleanExtra(LocationManager.EXTRA_LOCATION_ENABLED, false)
                else
                    locationManager.isLocationEnabled

            if (isEnabled) museumMapViewModel.onEvent(MuseumMapEvent.LocationAvailable)
            else museumMapViewModel.onEvent(MuseumMapEvent.LocationNotAvailable)
        }
    }

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ArtworksTrackerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    val navController = rememberNavController()
                    val snackbarHostState = remember { SnackbarHostState() }

                    Scaffold(
                        topBar = { TopBar() },
                        bottomBar = {
                            BottomBar(
                                navController = navController,
                                onMuseumMapExit = {
                                    tts?.stop()
                                    museumMapViewModel.onEvent(MuseumMapEvent.PauseTour)
                                },
                                onMuseumMapEntrance = { museumMapViewModel.onEvent(MuseumMapEvent.ResumeTour)}
                            )
                        },
                        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(it)
                        ) {
                            NavHost(
                                navController = navController,
                                startDestination = Screen.MuseumMapScreen.route
                            ) {
                                composable(route = Screen.MuseumMapScreen.route) {
                                    MuseumMapScreen(
                                        navController = navController,
                                        snackbarHostState = snackbarHostState,
                                        viewModel = museumMapViewModel,
                                        tts = tts,
                                        onBluetoothEnableRequest = {
                                            bluetoothEnablerLauncher.launch(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE))
                                        },
                                        onLocationEnableRequest = {
                                            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                                        }
                                    )
                                }

                                dialog(
                                    route = Screen.ArtworkScreen.route + "?artId={artId}&url={url}",
                                    dialogProperties = DialogProperties(
                                        usePlatformDefaultWidth = false,
                                        dismissOnBackPress = true,
                                        dismissOnClickOutside = true
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
                                        tts = tts,
                                        onDialogClosed = { museumMapViewModel.onEvent(MuseumMapEvent.ResumeTour) }
                                    )
                                }
                                composable(route = Screen.VisitedArtworksListScreen.route) {
                                    VisitedArtworksList(
                                        viewModel = hiltViewModel(),
                                        navController = navController
                                    )
                                }
                                composable(route = Screen.SettingsScreen.route) {
                                    SettingsScreen()
                                }
                            }
                        }
                    }
                }
            }
        }

        nsdManager = getSystemService(NsdManager::class.java)
        connectivityManager = getSystemService(ConnectivityManager::class.java)
        bluetoothManager = getSystemService(BluetoothManager::class.java)
        locationManager = getSystemService(LocationManager::class.java)

        tts = TextToSpeech(this) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val locale = this.resources.configuration.locales[0]
                tts!!.language = if (LanguageUtil.supportedLanguages.contains(locale.language)) locale else Locale.ENGLISH
            } else {
                tts = null
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        tts?.shutdown()
    }

    override fun onResume() {
        super.onResume()

        connectivityManager.requestNetwork(networkRequest, networkCallback)

        if (locationManager.isLocationEnabled) museumMapViewModel.onEvent(MuseumMapEvent.LocationAvailable)
        else museumMapViewModel.onEvent(MuseumMapEvent.LocationNotAvailable)

        registerReceiver(locationUpdateReceiver, IntentFilter(LocationManager.MODE_CHANGED_ACTION))

        if (bluetoothManager.adapter.isEnabled) museumMapViewModel.onEvent(MuseumMapEvent.BluetoothAvailable)
        else museumMapViewModel.onEvent(MuseumMapEvent.BluetoothNotAvailable)

        registerReceiver(bluetoothUpdateReceiver, IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED))

        museumMapViewModel.onEvent(MuseumMapEvent.ResumeTour)
    }

    override fun onPause() {
        super.onPause()

        nsdManager.stopServiceDiscovery(discoveryListener)
        museumMapViewModel.onEvent(MuseumMapEvent.BackendServerLost)

        connectivityManager.unregisterNetworkCallback(networkCallback)

        unregisterReceiver(bluetoothUpdateReceiver)

        unregisterReceiver(locationUpdateReceiver)

        museumMapViewModel.onEvent(MuseumMapEvent.PauseTour)
    }
}

@Composable
fun SettingsScreen(
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "SETTINGS",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
