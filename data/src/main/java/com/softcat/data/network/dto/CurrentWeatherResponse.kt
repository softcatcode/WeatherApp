package com.softcat.data.network.dto

import com.google.gson.annotations.SerializedName

data class CurrentWeatherResponse(
    @SerializedName("location") val location: LocationDto,
    @SerializedName("current") val value: CurrentWeatherDto
)
