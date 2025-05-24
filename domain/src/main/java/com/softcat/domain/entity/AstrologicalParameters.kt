package com.softcat.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class AstrologicalParameters(
    val sunriseTime: Long,
    val sunsetTime: Long,
    val moonriseTime: Long,
    val moonsetTime: Long,
    val moonPhase: String,
    val moonIllumination: Int,
): Parcelable