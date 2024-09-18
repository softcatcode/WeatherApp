package com.softcat.weatherapp.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WeatherParameters(
    val weatherType: WeatherType = WeatherType.Clouds,
    val temperatureMin: Float = 0f,
    val temperatureMax: Float = 0f,
    val humidityMin: Float = 0f,
    val humidityMax: Float = 100f,
    val windSpeedMin: Float = 0f,
    val windSpeedMax: Float = 100f,
    val precipitationsMin: Float = 0f,
    val precipitationsMax: Float = 100f,
    val snowVolumeMin: Float = 0f,
    val snowVolumeMax: Float = 100f
): Parcelable {
    val humidity: ClosedFloatingPointRange<Float>
        get() = humidityMin..humidityMax

    val windSpeed: ClosedFloatingPointRange<Float>
        get() = windSpeedMin..windSpeedMax

    val precipitations: ClosedFloatingPointRange<Float>
        get() = precipitationsMin..precipitationsMax

    val snowVolume: ClosedFloatingPointRange<Float>
        get() = snowVolumeMin..snowVolumeMax

    val temperature: ClosedFloatingPointRange<Float>
        get() = temperatureMin..temperatureMax

    fun updateHumidity(newValue: ClosedFloatingPointRange<Float>) = copy(
        humidityMin = newValue.start,
        humidityMax = newValue.endInclusive
    )

    fun updateWindSpeed(newValue: ClosedFloatingPointRange<Float>) = copy(
        windSpeedMin = newValue.start,
        windSpeedMax = newValue.endInclusive
    )

    fun updatePrecipitations(newValue: ClosedFloatingPointRange<Float>) = copy(
        precipitationsMin = newValue.start,
        precipitationsMax = newValue.endInclusive
    )

    fun updateSnowVolume(newValue: ClosedFloatingPointRange<Float>) = copy(
        snowVolumeMin = newValue.start,
        snowVolumeMax = newValue.endInclusive
    )

    fun updateMinTemperature(newValue: String): WeatherParameters {
        val newTemp = newValue.toFloatOrNull() ?: temperatureMin
        return if (newTemp <= temperatureMax)
            copy(temperatureMin = newTemp)
        else
            copy()
    }

    fun updateMaxTemperature(newValue: String): WeatherParameters {
        val newTemp = newValue.toFloatOrNull() ?: temperatureMax
        return if (newTemp >= temperatureMin)
            copy(temperatureMax = newTemp)
        else
            copy()
    }
}
