package com.softcat.weatherapp.presentation.hourly

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier

@Composable
fun HourlyWeatherStateContent(
    state: HourlyWeatherStore.State,
    paddings: PaddingValues
) {
    when (state) {
        is HourlyWeatherStore.State.Forecast -> {
            HourlyWeatherList(
                modifier = Modifier
                    .padding(paddings)
                    .fillMaxSize(),
                weatherList = state.hoursWeather
            )
        }
        HourlyWeatherStore.State.Initial -> {}
    }
}

@Composable
fun HourlyWeatherContent(
    component: HourlyWeatherComponent
) {
    val state = component.model.collectAsState()
    Scaffold(
        topBar = {
            HourlyWeatherTopBar(onBackClicked = {
                component.back()
            })
        }
    ) { paddings ->
        HourlyWeatherStateContent(
            state = state.value,
            paddings = paddings
        )
    }
}