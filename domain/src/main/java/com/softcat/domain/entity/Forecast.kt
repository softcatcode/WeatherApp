package com.softcat.domain.entity

data class Forecast(
    val weather: Weather,
    val hourly: List<List<Weather>>,
    val upcoming: List<Weather>
)
