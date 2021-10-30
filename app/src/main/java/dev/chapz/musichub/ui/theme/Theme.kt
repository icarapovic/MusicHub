package dev.chapz.musichub.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    surface = Color.DarkGray,
)
private val LightColorPalette = lightColors()

@Composable
fun MusicHubTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable ()->Unit ) {
    val colors = if(darkTheme) DarkColorPalette else LightColorPalette

    MaterialTheme(
        colors = colors,
        content = content
    )
}