package com.gramaurja2.app.navigation

sealed class Route(val path: String) {
    data object Splash : Route("splash")
    data object Onboarding : Route("onboarding")
    data object Home : Route("home")
    data object Report : Route("report")
    data object Pump : Route("pump")
    data object Ai : Route("ai")
    data object Notifications : Route("notifications")
    data object Profile : Route("profile")
    data object Settings : Route("settings")
}

data class BottomTab(val route: Route, val label: String, val symbol: String)

val bottomTabs = listOf(
    BottomTab(Route.Home, "Home", "H"),
    BottomTab(Route.Pump, "Pump", "P"),
    BottomTab(Route.Ai, "AI Tips", "AI"),
    BottomTab(Route.Notifications, "Alerts", "!"),
    BottomTab(Route.Profile, "Profile", "U")
)
