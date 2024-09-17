package com.softcat.weatherapp.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WeatherParameters(
    val weatherType: WeatherType = WeatherType.Clouds,
    val minTemp: Float = 0f,
    val maxTemp: Float = 0f,
    val humidity: Float = 0f,
    val windSpeed: Float = 0f,
    val precipitations: Float = 0f,
    val snowVolume: Float = 0f
): Parcelable
