package com.softcat.data.network.dto

import com.google.gson.annotations.SerializedName

data class AstrologicalParametersDto(
    @SerializedName("sunrise") val sunriseTime: String,
    @SerializedName("sunset") val sunsetTime: String,
    @SerializedName("moonrise") val moonriseTime: String,
    @SerializedName("moonset") val moonsetTime: String
)
