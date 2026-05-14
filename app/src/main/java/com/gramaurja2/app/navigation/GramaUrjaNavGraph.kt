package com.gramaurja2.app.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.gramaurja2.app.ui.screens.AiSuggestionsScreen
import com.gramaurja2.app.ui.screens.HomeDashboardScreen
import com.gramaurja2.app.ui.screens.NotificationsScreen
import com.gramaurja2.app.ui.screens.OnboardingScreen
import com.gramaurja2.app.ui.screens.PowerReportScreen
import com.gramaurja2.app.ui.screens.ProfileScreen
import com.gramaurja2.app.ui.screens.PumpTimerScreen
import com.gramaurja2.app.ui.screens.SettingsScreen
import com.gramaurja2.app.ui.screens.SplashScreen

@Composable
fun GramaUrjaNavGraph() {
    val navController = rememberNavController()
    val backStack by navController.currentBackStackEntryAsState()
    val currentRoute = backStack?.destination?.route
    val showBottomBar = currentRoute in bottomTabs.map { it.route.path }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    bottomTabs.forEach { tab ->
                        val selected = backStack?.destination?.hierarchy?.any { it.route == tab.route.path } == true
                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                navController.navigate(tab.route.path) {
                                    popUpTo(Route.Home.path) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Text(tab.symbol) },
                            label = { Text(tab.label) }
                        )
                    }
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Route.Splash.path,
            modifier = Modifier.padding(padding)
        ) {
            composable(Route.Splash.path) {
                SplashScreen(onNavigate = { route ->
                    navController.navigate(route) {
                        popUpTo(Route.Splash.path) { inclusive = true }
                    }
                })
            }
            composable(Route.Onboarding.path) {
                OnboardingScreen(onComplete = {
                    navController.navigate(Route.Home.path) {
                        popUpTo(Route.Onboarding.path) { inclusive = true }
                    }
                })
            }
            composable(Route.Home.path) {
                HomeDashboardScreen(
                    onOpenReport = { navController.navigate(Route.Report.path) },
                    onOpenSettings = { navController.navigate(Route.Settings.path) }
                )
            }
            composable(Route.Report.path) {
                PowerReportScreen(onBack = navController::popBackStack)
            }
            composable(Route.Pump.path) { PumpTimerScreen() }
            composable(Route.Ai.path) { AiSuggestionsScreen() }
            composable(Route.Notifications.path) { NotificationsScreen() }
            composable(Route.Profile.path) {
                ProfileScreen(onOpenSettings = { navController.navigate(Route.Settings.path) })
            }
            composable(Route.Settings.path) {
                SettingsScreen(
                    onBack = navController::popBackStack,
                    onChangeZones = { navController.navigate(Route.Onboarding.path) }
                )
            }
        }
    }
}
