package com.softcat.weatherapp.data.network.dto

import com.google.gson.annotations.SerializedName

data class DaysWeatherDto(
    @SerializedName("avgtemp_c") val tempC: Float,
    @SerializedName("condition") val condition: ConditionDto,

)
