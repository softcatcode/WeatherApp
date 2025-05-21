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
    val sunriseTime: String,
    val sunsetTime: String,
    val moonriseTime: String,
    val moonsetTime: String,
    val moonIllumination: Int,
    val isSunUp: Int,
    val isMoonUp: Int,
    val moonPhase: String,
    val rainChance: Int,
) {
    companion object {
        const val UNSPECIFIED_ID = 0
    }
}