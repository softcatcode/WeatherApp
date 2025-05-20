package com.softcat.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WeatherParameters(
    val weatherType: WeatherType = WeatherType.Any,
    val temperatureMin: Float = MIN_TEMPERATURE,
    val temperatureMax: Float = MAX_TEMPERATURE,
    val humidityMin: Int = MIN_HUMIDITY,
    val humidityMax: Int = MAX_HUMIDITY,
    val windSpeedMin: Int = MIN_WIND_SPEED,
    val windSpeedMax: Int = MAX_WIND_SPEED,
    val precipitationsMin: Int = MIN_PRECIPITATIONS,
    val precipitationsMax: Int = MAX_PRECIPITATIONS,
    val snowVolumeMin: Int = MIN_SNOW_VOLUME,
    val snowVolumeMax: Int = MAX_SNOW_VOLUME
): Parcelable {
    val humidity: IntRange
        get() = humidityMin..humidityMax

    val windSpeed: IntRange
        get() = windSpeedMin..windSpeedMax

    val precipitations: IntRange
        get() = precipitationsMin..precipitationsMax

    val snowVolume: IntRange
        get() = snowVolumeMin..snowVolumeMax

    val temperature: ClosedFloatingPointRange<Float>
        get() = temperatureMin..temperatureMax

    fun updateHumidity(newValue: IntRange) = copy(
        humidityMin = newValue.first,
        humidityMax = newValue.last
    )

    fun updateWindSpeed(newValue: IntRange) = copy(
        windSpeedMin = newValue.first,
        windSpeedMax = newValue.last
    )

    fun updatePrecipitations(newValue: IntRange) = copy(
        precipitationsMin = newValue.first,
        precipitationsMax = newValue.last
    )

    fun updateSnowVolume(newValue: IntRange) = copy(
        snowVolumeMin = newValue.first,
        snowVolumeMax = newValue.last
    )

    fun updateMinTemperature(newValue: String): WeatherParameters {
        val newTemp = newValue.toFloatOrNull() ?: temperatureMin
        return if (newTemp in MIN_TEMPERATURE..temperatureMax)
            copy(temperatureMin = newTemp)
        else
            copy()
    }

    fun updateMaxTemperature(newValue: String): WeatherParameters {
        val newTemp = newValue.toFloatOrNull() ?: temperatureMax
        return if (newTemp in temperatureMin..MAX_TEMPERATURE)
            copy(temperatureMax = newTemp)
        else
            copy()
    }

    companion object {
        const val MIN_WIND_SPEED = 0
        const val MAX_WIND_SPEED = 30
        const val MIN_HUMIDITY = 0
        const val MAX_HUMIDITY = 100
        const val MIN_PRECIPITATIONS = 0
        const val MAX_PRECIPITATIONS = 100
        const val MIN_SNOW_VOLUME = 0
        const val MAX_SNOW_VOLUME = 1000
        const val MIN_TEMPERATURE = -40f
        const val MAX_TEMPERATURE = 40f
    }
}
