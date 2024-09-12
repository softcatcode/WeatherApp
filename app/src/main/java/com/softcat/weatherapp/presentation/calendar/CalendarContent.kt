package com.softcat.weatherapp.presentation.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.softcat.weatherapp.domain.entity.WeatherType

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun CalendarContent() {
    var isExpanded by rememberSaveable { mutableStateOf(false) }
    IconButton(
        modifier = Modifier
            .padding(30.dp)
            .background(Color.Blue),
        onClick = { isExpanded = true }
    ) {
        Icon(
            modifier = Modifier.size(200.dp),
            imageVector = Icons.Filled.Search,
            contentDescription = null,
            tint = Color.Red
        )
    }
    CalendarBottomSheet(
        isExpanded = isExpanded,
        onDismiss = { isExpanded = false },
        currentWeatherType = WeatherType.Clouds,
        minTemperature = "12",
        maxTemperature = "24",
        onMinTempChange = {},
        onMaxTempChange = {},
        onWeatherTypeSelected = {},
        onWindSpeedValueChange = {},
        onHumidityValueChange = {},
        onSnowVolumeValueChange = {},
        onPrecipitationsValueChange = {},
        precipitations = 0f,
        snowVolume = 0f,
        windSpeed = 0f,
        humidity = 0f
    )
}