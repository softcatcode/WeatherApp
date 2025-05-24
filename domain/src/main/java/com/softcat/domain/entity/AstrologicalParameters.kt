package com.softcat.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class AstrologicalParameters(
    val sunriseTime: String,
    val sunsetTime: String,
    val moonriseTime: String,
    val moonsetTime: String,
    val moonPhase: String,
    val moonIllumination: Int,
): Parcelable