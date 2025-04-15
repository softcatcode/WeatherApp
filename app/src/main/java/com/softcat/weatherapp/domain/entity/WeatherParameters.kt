package com.softcat.weatherapp.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WeatherParameters(
    val weatherType: WeatherType = WeatherType.Any,
    val temperatureMin: Float = MIN_TEMPERATURE,
    val temperatureMax: Float = MAX_TEMPERATURE,
    val humidityMin: Float = MIN_HUMIDITY,
    val humidityMax: Float = MAX_HUMIDITY,
    val windSpeedMin: Float = MIN_WIND_SPEED,
    val windSpeedMax: Float = MAX_WIND_SPEED,
    val precipitationsMin: Float = MIN_PRECIPITATIONS,
    val precipitationsMax: Float = MAX_PRECIPITATIONS,
    val snowVolumeMin: Float = MIN_SNOW_VOLUME,
    val snowVolumeMax: Float = MAX_SNOW_VOLUME
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
        const val MIN_WIND_SPEED = 0f
        const val MAX_WIND_SPEED = 30f
        const val MIN_HUMIDITY = 0f
        const val MAX_HUMIDITY = 100f
        const val MIN_PRECIPITATIONS = 0f
        const val MAX_PRECIPITATIONS = 1000f
        const val MIN_SNOW_VOLUME = 0f
        const val MAX_SNOW_VOLUME = 1000f
        const val MIN_TEMPERATURE = -40f
        const val MAX_TEMPERATURE = 40f
    }
}
