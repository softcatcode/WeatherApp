package com.softcat.weatherapp.data.mapper

import com.softcat.weatherapp.data.network.dto.WeatherCurrentDto
import com.softcat.weatherapp.data.network.dto.WeatherDto
import com.softcat.weatherapp.data.network.dto.WeatherForecastDto
import com.softcat.weatherapp.domain.entity.Forecast
import com.softcat.weatherapp.domain.entity.Weather
import java.util.Calendar
import java.util.Date

fun WeatherCurrentDto.toEntity() = value.toEntity()

fun Long.toCalendar(): Calendar = Calendar.getInstance().apply {
    time = Date(this@toCalendar * 1000)
}

fun WeatherDto.toEntity(): Weather = Weather(
    tempC = tempC,
    conditionText = condition.description,
    conditionUrl = condition.iconUrl.correctIconUrl(),
    date = date.toCalendar(),
    formattedDate = "",
    humidity = humidity,
    windSpeed = windSpeed,
    snowVolume = snowVolume,
    precipitations = precipitations
)

fun WeatherForecastDto.toEntity() = Forecast(
    weather = current.toEntity(),
    upcoming = forecastDto.days.drop(1).map { dayDto ->
        with (dayDto.weather) {
            Weather(
                tempC = tempC,
                conditionText = condition.description,
                conditionUrl = condition.iconUrl.correctIconUrl(),
                date = dayDto.date.toCalendar(),
                formattedDate = dayDto.formattedDate,
                humidity = humidity,
                windSpeed = windSpeed,
                snowVolume = snowVolume,
                precipitations = precipitations
            )
        }
    }
)

private fun String.correctIconUrl() = "https:$this".replace("64x64", "128x128")