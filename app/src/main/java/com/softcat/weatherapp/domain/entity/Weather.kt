package com.softcat.weatherapp.domain.entity

import com.google.gson.annotations.SerializedName
import java.util.Calendar

data class Weather(
    val type: WeatherType,
    val tempC: Float,
    val conditionText: String,
    val conditionUrl: String,
    val date: Calendar,
    val formattedDate: String,
    val humidity: Float,
    val windSpeed: Float,
    val snowVolume: Float,
    val precipitations: Float,
)
