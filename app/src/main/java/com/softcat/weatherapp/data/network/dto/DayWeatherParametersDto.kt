package com.softcat.weatherapp.data.network.dto

import com.google.gson.annotations.SerializedName

data class DayWeatherParametersDto(
    @SerializedName("avgtemp_c") val tempC: Float,
    @SerializedName("feelslike_c") val feelsLike: Float,
    @SerializedName("avghumidity") val humidity: Float,
    @SerializedName("maxwind_mph") val windSpeed: Float,
    @SerializedName("totalsnow_cm") val snowVolume: Float,
    @SerializedName("totalprecip_mm") val precipitations: Float,
    @SerializedName("condition") val condition: ConditionDto,
)
