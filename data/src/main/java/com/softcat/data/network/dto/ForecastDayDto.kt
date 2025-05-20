package com.softcat.data.network.dto

import com.google.gson.annotations.SerializedName

data class ForecastDayDto(
    @SerializedName("date") val formattedDate: String,
    @SerializedName("date_epoch") val date: Long,
    @SerializedName("day") val weather: DayWeatherParametersDto,
    @SerializedName("astro") val astrologicalParams: AstronomicParametersDto,
    @SerializedName("hour") val hoursWeather: List<CurrentWeatherDto>
)
