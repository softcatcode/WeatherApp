package com.softcat.data.network.dto

import com.google.gson.annotations.SerializedName

data class DayWeatherParametersDto(
    @SerializedName("maxtemp_c") val maxTempC: Float,
    @SerializedName("maxtemp_f") val maxTempF: Float,
    @SerializedName("mintemp_c") val minTempC: Float,
    @SerializedName("mintemp_f") val minTempF: Float,
    @SerializedName("avgtemp_c") val avgTempC: Float,
    @SerializedName("avgtemp_f") val avgTempF: Float,

    @SerializedName("maxwind_mph") val maxWindMilesPerHour: Float,
    @SerializedName("maxwind_kph") val maxWindKilometersPerHour: Float,

    @SerializedName("totalprecip_mm") val totalPrecipitationsMillimeters: Float,
    @SerializedName("totalprecip_in") val totalPrecipitationsInches: Float,
    @SerializedName("totalsnow_cm") val totalSnowCm: Float,

    @SerializedName("avgvis_km") val avgVisionKm: Float,
    @SerializedName("avgvis_miles") val avgVisionMiles: Float,

    @SerializedName("daily_will_it_rain") val willItRain: Int,
    @SerializedName("daily_chance_of_rain") val rainChance: Int,
    @SerializedName("daily_will_it_snow") val willItSnow: Int,
    @SerializedName("daily_chance_of_snow") val snowChance: Int,

    @SerializedName("avghumidity") val avgHumidity: Int,
    @SerializedName("condition") val condition: ConditionDto,
    @SerializedName("uv") val uvIndex: Float
)
