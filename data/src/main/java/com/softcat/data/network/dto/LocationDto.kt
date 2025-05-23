package com.softcat.data.network.dto

import com.google.gson.annotations.SerializedName

data class LocationDto(
    @SerializedName("name") val name: String,
    @SerializedName("region") val region: String,
    @SerializedName("country") val country: String,
    @SerializedName("lat") val latitude: Float,
    @SerializedName("lon") val longitude: Float,
    @SerializedName("tz_id") val timezoneId: String,
    @SerializedName("localtime_epoch") val localtimeEpoch: Long,
    @SerializedName("localtime") val localtime: String,
)