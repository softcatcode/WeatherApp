package com.softcat.data.network.dto

import com.google.gson.annotations.SerializedName

data class AstronomicParametersDto(
    @SerializedName("sunrise") val sunriseTime: String,
    @SerializedName("sunset") val sunsetTime: String,
    @SerializedName("moonrise") val moonriseTime: String,
    @SerializedName("moonset") val moonsetTime: String,
    @SerializedName("moon_phase") val moonPhase: String,
    @SerializedName("moon_illumination") val moonIllumination: Int,
)
