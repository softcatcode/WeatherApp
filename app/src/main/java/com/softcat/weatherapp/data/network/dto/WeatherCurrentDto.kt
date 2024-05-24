package com.softcat.weatherapp.data.network.dto

import com.google.gson.annotations.SerializedName

data class WeatherCurrentDto(
    @SerializedName("current") val value: WeatherDto
)
