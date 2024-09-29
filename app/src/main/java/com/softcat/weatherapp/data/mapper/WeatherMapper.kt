package com.softcat.weatherapp.data.mapper

import com.softcat.weatherapp.data.network.dto.AstrologicalParametersDto
import com.softcat.weatherapp.data.network.dto.WeatherCurrentDto
import com.softcat.weatherapp.data.network.dto.WeatherDto
import com.softcat.weatherapp.data.network.dto.WeatherForecastDto
import com.softcat.weatherapp.domain.entity.AstrologicalParameters
import com.softcat.weatherapp.domain.entity.Forecast
import com.softcat.weatherapp.domain.entity.Weather
import com.softcat.weatherapp.domain.entity.weatherTypeOf
import java.util.Calendar
import java.util.Date

fun WeatherCurrentDto.toEntity() = value.toEntity()

fun Long.toCalendar(): Calendar = Calendar.getInstance().apply {
    time = Date(this@toCalendar * 1000)
}

fun AstrologicalParametersDto.toEntity() = AstrologicalParameters(
    sunriseTime = sunriseTime,
    sunsetTime = sunsetTime,
    moonriseTime = moonriseTime,
    moonsetTime = moonsetTime,
)

fun WeatherDto.toEntity(): Weather = Weather(
    tempC = tempC,
    feelsLike = feelsLike,
    conditionText = condition.description,
    conditionUrl = condition.iconUrl.correctIconUrl(),
    date = (date ?: 0L).toCalendar(),
    formattedDate = "",
    humidity = humidity,
    windSpeed = windSpeed,
    snowVolume = snowVolume,
    precipitations = precipitations,
    type = weatherTypeOf(condition.code),
    astrologicalParams = null
)

fun WeatherForecastDto.toEntity(): Forecast {
    val upcoming = forecastDto.days.map { dayDto ->
        with (dayDto.weather) {
            Weather(
                tempC = tempC,
                feelsLike = feelsLike,
                conditionText = condition.description,
                conditionUrl = condition.iconUrl.correctIconUrl(),
                date = dayDto.date.toCalendar(),
                formattedDate = dayDto.formattedDate,
                humidity = humidity,
                windSpeed = windSpeed,
                snowVolume = snowVolume,
                precipitations = precipitations,
                type = weatherTypeOf(condition.code),
                astrologicalParams = dayDto.astrologicalParams.toEntity()
            )
        }
    }
    val hourlyWeather = forecastDto.days.map { dayDto ->
        dayDto.hoursWeather.map {
            it.toEntity().copy(date = (it.timeEpoch ?: 0L).toCalendar())
        }
    }
    return Forecast(
        weather = current.toEntity(),
        upcoming = upcoming,
        hourly = hourlyWeather
    )
}

private fun String.correctIconUrl() = "https:$this".replace("64x64", "128x128")