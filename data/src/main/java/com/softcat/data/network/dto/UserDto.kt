package com.softcat.data.network.dto

import com.google.gson.annotations.SerializedName

data class UserDto(
    @SerializedName("id") val userId: String,
    @SerializedName("name") val name: String,
    @SerializedName("password") val password: String,
    @SerializedName("register_date") val registerEpoch: Long,
    @SerializedName("status") val role: String,
    @SerializedName("email") val email: String
)