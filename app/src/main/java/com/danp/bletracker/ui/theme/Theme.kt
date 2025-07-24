package com.danp.bletracker.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme

import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFBB86FC),
    onPrimary = Color.White,
    background = Color.Black,
    surface = Color.DarkGray,
    onSurface = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF6200EE),
    onPrimary = Color.White,
    background = Color.White,
    surface = Color.White,
    onSurface = Color.Black
)

@Composable

fun BLETrackerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(), // <- esto es lo importante
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        darkColorScheme() // colores oscuros
    } else {
        lightColorScheme() // colores claros
    }

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}