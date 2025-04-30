package com.softcat.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Calendar

@Parcelize
data class Weather(
    val type: WeatherType,
    val tempC: Float,
    val feelsLike: Float,
    val conditionText: String,
    val conditionUrl: String,
    val date: Calendar,
    val formattedDate: String,
    val humidity: Float,
    val windSpeed: Float,
    val snowVolume: Float,
    val precipitations: Float,
    val astrologicalParams: AstrologicalParameters?
): Parcelable
