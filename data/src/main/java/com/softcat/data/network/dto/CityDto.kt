package com.softcat.data.network.dto

import com.google.gson.annotations.SerializedName

data class CityDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("country") val country: String,
    @SerializedName("lat") val latitude: Float,
    @SerializedName("lon") val longitude: Float,
)
