package com.softcat.weatherapp.data.network.dto

import com.google.gson.annotations.SerializedName

data class DayForecastDto(
    @SerializedName("forecastday") val days: List<DayDto>
)
