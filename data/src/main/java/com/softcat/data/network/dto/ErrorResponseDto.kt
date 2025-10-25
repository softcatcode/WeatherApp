package com.softcat.data.network.dto

import com.google.gson.annotations.SerializedName

data class ErrorResponseDto(
    @SerializedName("error") val error: ErrorDto
)