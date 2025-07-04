package com.softcat.database.model

data class WeatherDbModel(
    val id: Int,
    val timeEpoch: Long,
    val cityId: Int,
    val type: Int,
    val avgTemp: Float,
    val humidity: Int,
    val windSpeed: Int,
    val snowVolume: Int,
    val precipitations: Int,
    val vision: Float,
    val sunriseTime: Long,
    val sunsetTime: Long,
    val moonriseTime: Long,
    val moonsetTime: Long,
    val moonIllumination: Int,
    val moonPhase: String,
    val rainChance: Int,
) {
    companion object {
        const val UNSPECIFIED_ID = 0
    }
}