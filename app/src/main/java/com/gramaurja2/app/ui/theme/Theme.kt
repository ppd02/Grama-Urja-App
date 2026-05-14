package com.gramaurja2.app.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightScheme = lightColorScheme(
    primary = Forest,
    onPrimary = Color.White,
    secondary = Sky,
    onSecondary = Color.White,
    tertiary = Solar,
    background = Cloud,
    onBackground = Ink,
    surface = Color.White,
    onSurface = Ink,
    error = Alert
)

private val DarkScheme = darkColorScheme(
    primary = Color(0xFF47D993),
    onPrimary = Night,
    secondary = Color(0xFF4FC3F7),
    onSecondary = Night,
    tertiary = Solar,
    background = Night,
    onBackground = Color(0xFFE9FFF5),
    surface = NightPanel,
    onSurface = Color(0xFFE9FFF5),
    error = Color(0xFFFF8A7A)
)

@Composable
fun GramaUrjaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkScheme else LightScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            window.navigationBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }
    MaterialTheme(colorScheme = colorScheme, typography = GramaTypography, content = content)
}
