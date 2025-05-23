package com.softcat.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Calendar

@Parcelize
data class Weather(
    val avgTemp: Float,
    val conditionCode: Int,
    val conditionText: String,
    val conditionUrl: String,
    val date: Calendar,
    val formattedDate: String,
    val vision: Float,
    val humidity: Int,
    val windSpeed: Int,
    val snowVolume: Int,
    val precipitations: Int,
    val astrologicalParams: AstrologicalParameters,
    val rainChance: Int,
): Parcelable
