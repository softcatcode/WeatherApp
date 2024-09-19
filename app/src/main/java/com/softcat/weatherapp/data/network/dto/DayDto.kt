package com.softcat.weatherapp.data.network.dto

import com.google.gson.annotations.SerializedName

data class DayDto(
    @SerializedName("date") val formattedDate: String,
    @SerializedName("date_epoch") val date: Long,
    @SerializedName("day") val weather: DayWeatherParametersDto
)
