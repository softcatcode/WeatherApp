package com.softcat.domain.entity

data class Forecast(
    val weather: CurrentWeather,
    val hourly: List<List<CurrentWeather>>,
    val upcoming: List<Weather>
)
