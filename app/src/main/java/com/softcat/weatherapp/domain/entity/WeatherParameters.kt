package com.softcat.weatherapp.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WeatherParameters(
    val weatherType: WeatherType,
    val minTemp: Float,
    val maxTemp: Float,
    val humidity: Float,
    val windSpeed: Float,
    val snowVolume: Float
): Parcelable
