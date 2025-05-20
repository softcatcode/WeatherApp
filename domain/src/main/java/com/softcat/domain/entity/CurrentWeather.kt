package com.softcat.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CurrentWeather(
    val timeEpoch: Long,
    val tempC: Float,
    val feelsLike: Int,
    val isDay: Boolean,
    val conditionCode: Int,
    val conditionUrl: String,
    val conditionText: String,
    val windSpeed: Int,
    val precipitations: Int,
    val snow: Int?,
    val humidity: Int,
    val cloud: Int,
    val vision: Float,
): Parcelable