package com.softcat.data.network.dto

import com.google.gson.annotations.SerializedName

data class DeleteUserResponseDto(
    @SerializedName("id") val userId: String,
    @SerializedName("name") val name: String,
    @SerializedName("delete_date") val deleteEpoch: Long
)