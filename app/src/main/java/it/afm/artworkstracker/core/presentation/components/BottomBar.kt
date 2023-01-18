package it.afm.artworkstracker.core.presentation.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import it.afm.artworkstracker.util.Screen

@Composable
fun BottomBar(
    navController: NavHostController,
    onMuseumMapExit: () -> Unit,
    onMuseumMapEntrance: () -> Unit
) {

    val screens = listOf(
        Screen.MuseumMapScreen,
        Screen.VisitedArtworksListScreen,
        Screen.SettingsScreen
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar {
        screens.forEach { screen ->
            AddItem(
                screen = screen,
                currentDestination = currentDestination,
                navController = navController,
                onMuseumMapExit = onMuseumMapExit,
                onMuseumMapEntrance = onMuseumMapEntrance
            )
        }
    }
}

@Composable
fun RowScope.AddItem(
    screen: Screen,
    currentDestination: NavDestination?,
    navController: NavHostController,
    onMuseumMapExit: () -> Unit,
    onMuseumMapEntrance: () -> Unit
) {
    NavigationBarItem(label = {
        val label = screen.stringId?.let { stringResource(id = it) }
        label?.let { Text(text = it) }
    },
        icon = {
            screen.imageVector?.let {
                Icon(
                    imageVector = it,
                    contentDescription = "Navigation Icon"
                )
            }
        },
        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
        onClick = {
            if (screen.route == Screen.MuseumMapScreen.route) onMuseumMapEntrance()
            else onMuseumMapExit()

            navController.navigate(screen.route) {
                popUpTo(navController.graph.findStartDestination().id)
                launchSingleTop = true
            }
        }
    )
}