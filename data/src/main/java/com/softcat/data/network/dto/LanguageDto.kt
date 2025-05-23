package com.softcat.data.network.dto

import com.google.gson.annotations.SerializedName

data class LanguageDto(
    @SerializedName("lang_name") val name: String,
    @SerializedName("lang_iso") val label: String,
    @SerializedName("day_text") val dayText: String,
    @SerializedName("night_text") val nightText: String
)