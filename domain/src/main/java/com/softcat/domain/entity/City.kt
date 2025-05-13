package com.softcat.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class City(
    val id: Int,
    val name: String,
    val country: String,
    val latitude: Float,
    val longitude: Float
): Parcelable
