package com.example.angelix_vasquez_p2_p1.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = BluePrimary,
    onPrimary = White,
    secondary = BlueSecondary,
    onSecondary = Black,
    background = White,
    onBackground = Black,
    surface = White,
    onSurface = Black
)

private val DarkColors = darkColorScheme(
    primary = BluePrimary,
    onPrimary = White,
    secondary = BlueSecondary,
    onSecondary = Black,
    background = Black,
    onBackground = White,
    surface = Black,
    onSurface = White
)

@Composable
fun AngelixVasquezP2P1Theme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
