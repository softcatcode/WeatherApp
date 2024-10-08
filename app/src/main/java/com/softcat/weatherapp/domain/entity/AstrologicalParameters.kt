package com.softcat.weatherapp.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class AstrologicalParameters(
    val sunriseTime: String,
    val sunsetTime: String,
    val moonriseTime: String,
    val moonsetTime: String
): Parcelable