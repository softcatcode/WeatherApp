package com.softcat.database.model

data class CurrentWeatherDbModel(
    val id: Int,
    val cityId: Int,
    val timeEpoch: Long,
    val tempC: Float,
    val feelsLike: Int,
    val isDay: Int,
    val type: Int,
    val windSpeed: Int,
    val precipitations: Int,
    val snow: Int?,
    val humidity: Int,
    val cloud: Int,
    val vision: Float,
) {
    companion object {
        const val UNSPECIFIED_ID = 0
    }
}