package com.softcat.data.network.dto

import com.google.gson.annotations.SerializedName

data class ConditionDto(
    @SerializedName("text") val description: String,
    @SerializedName("icon") val iconUrl: String,
    @SerializedName("code") val code: Int
)
