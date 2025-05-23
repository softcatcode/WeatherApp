package com.softcat.data.network.dto

import com.google.gson.annotations.SerializedName

data class WeatherForecastDto(
    @SerializedName("location") val location: LocationDto,
    @SerializedName("current") val current: CurrentWeatherDto,
    @SerializedName("forecast") val forecastDto: DayForecastDto
)