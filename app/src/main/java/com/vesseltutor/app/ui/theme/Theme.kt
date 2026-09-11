package com.vesseltutor.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = NavyPrimary,
    onPrimary = Color.White,
    secondary = OceanAccent,
    background = SurfaceLight,
    surface = Color.White,
    error = AlertRed
)

private val DarkColors = darkColorScheme(
    primary = OceanAccent,
    onPrimary = Color.Black,
    secondary = NavyPrimaryDark,
    background = SurfaceDark,
    surface = SurfaceDark,
    error = AlertRed
)

@Composable
fun VesselTutorTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors
    MaterialTheme(
        colorScheme = colors,
        typography = VesselTutorTypography,
        content = content
    )
}
