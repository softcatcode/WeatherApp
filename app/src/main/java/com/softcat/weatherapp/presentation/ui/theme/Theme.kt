package com.softcat.weatherapp.presentation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color.Companion.White

private val DarkColorScheme = darkColorScheme(
    background = White,
    onBackground = CustomPurple,
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = SoftGreen,
    onSurface = Pink80,
)

private val LightColorScheme = lightColorScheme(
    background = White,
    onBackground = OutlineColorFocused,
    primary = CustomPurple,
    secondary = PurpleGrey40,
    tertiary = SoftGreen,
    onSurface = Pink80,
)

/* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
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