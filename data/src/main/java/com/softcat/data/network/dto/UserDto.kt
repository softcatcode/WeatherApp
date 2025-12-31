package com.softcat.data.network.dto

import com.google.gson.annotations.SerializedName

data class UserDto(
    @SerializedName("id") val userId: String,
    @SerializedName("name") val name: String,
    @SerializedName("password") val password: String,
    @SerializedName("registerEpoch") val registerEpoch: Long,
    @SerializedName("role") val role: String,
    @SerializedName("email") val email: String
)