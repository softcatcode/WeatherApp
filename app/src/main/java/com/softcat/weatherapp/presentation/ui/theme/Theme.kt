package com.softcat.weatherapp.presentation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    background = White,
    onBackground = CalendarPurple,
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40,
    onSurface = Pink80,
)

private val LightColorScheme = lightColorScheme(
    background = White,
    onBackground = CalendarPurple,
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40,
    onSurface = Pink80,
)

/* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
*/

@Composable
fun WeatherAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}