package com.softcat.data.network.dto

import com.google.gson.annotations.SerializedName

data class WeatherTypeInfoDto(
    @SerializedName("code") val code: Int,
    @SerializedName("day") val day: String,
    @SerializedName("night") val night: String,
    @SerializedName("icon") val iconCode: Int,
    @SerializedName("languages") val languages: List<LanguageDto>
)