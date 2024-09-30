package com.softcat.weatherapp.presentation.hourly

import kotlinx.coroutines.flow.StateFlow

interface HourlyWeatherComponent {
    fun back()

    val model: StateFlow<HourlyWeatherStore.State>
}