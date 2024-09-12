package com.softcat.weatherapp.domain.entity

data class WeatherParameters(
    val minTemp: Float,
    val maxTemp: Float,
    val type: WeatherType,
)
