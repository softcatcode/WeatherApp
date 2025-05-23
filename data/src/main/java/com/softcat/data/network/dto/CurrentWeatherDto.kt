package com.softcat.data.network.dto

import com.google.gson.annotations.SerializedName

data class CurrentWeatherDto(
    // if per-hour weather
    @SerializedName("time") val time: String?,
    @SerializedName("time_epoch") val timeEpoch: Long?,
    @SerializedName("snow_cm") val snow: Float?,

    // if the current weather
    @SerializedName("last_updated_epoch") val lastUpdatedEpoch: Long?,
    @SerializedName("last_updated") val lastUpdateTime: String?,

    // common parameters
    @SerializedName("temp_c") val tempC: Float,
    @SerializedName("temp_f") val tempF: Float,
    @SerializedName("is_day") val isDay: Int,
    @SerializedName("condition") val condition: ConditionDto,
    @SerializedName("wind_mph") val windMilesPerHour: Float,
    @SerializedName("wind_kph") val windKilometersPerHour: Float,
    @SerializedName("wind_degree") val windDegree: Int,
    @SerializedName("wind_dir") val windDirection: String,
    @SerializedName("pressure_mb") val pressureMillibars: Float,
    @SerializedName("pressure_in") val pressureInches: Float,
    @SerializedName("precip_mm") val precipitationsMillimeters: Float,
    @SerializedName("precip_in") val precipitationsInches: Float,
    @SerializedName("humidity") val humidity: Float,
    @SerializedName("cloud") val cloud: Int,
    @SerializedName("feelslike_c") val feelsLikeC: Float,
    @SerializedName("feelslike_f") val feelsLikeF: Float,
    @SerializedName("windchill_c") val windchillC: Float,
    @SerializedName("windchill_f") val windchillF: Float,
    @SerializedName("heatindex_c") val heatIndexC: Float,
    @SerializedName("heatindex_f") val heatIndexF: Float,
    @SerializedName("dewpoint_c") val dewPointC: Float,
    @SerializedName("dewpoint_f") val dewPointF: Float,
    @SerializedName("vis_km") val visionKilometers: Float,
    @SerializedName("vis_miles") val visionMiles: Float,
    @SerializedName("uv") val uvIndex: Float,
    @SerializedName("gust_mph") val gustMilesPerHour: Float,
    @SerializedName("gust_kph") val gustKilometersPerHour: Float,
)
