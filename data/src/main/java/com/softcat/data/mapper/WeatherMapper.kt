package com.softcat.data.mapper

import com.softcat.data.network.dto.AstronomicParametersDto
import com.softcat.data.network.dto.CurrentWeatherResponse
import com.softcat.data.network.dto.CurrentWeatherDto
import com.softcat.data.network.dto.WeatherForecastDto
import com.softcat.data.network.dto.WeatherTypeInfoDto
import com.softcat.domain.entity.AstrologicalParameters
import com.softcat.domain.entity.CurrentWeather
import com.softcat.domain.entity.Forecast
import com.softcat.domain.entity.Weather
import com.softcat.domain.entity.WeatherTypeInfo
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

fun CurrentWeatherResponse.toEntity() = value.toEntity(location.localtimeEpoch)

fun Long.toCalendar(): Calendar = Calendar.getInstance().apply {
    time = Date(this@toCalendar * 1000)
}

fun AstronomicParametersDto.toEntity() = AstrologicalParameters(
    sunriseTime = sunriseTime,
    sunsetTime = sunsetTime,
    moonriseTime = moonriseTime,
    moonsetTime = moonsetTime,
    isSunUp = isSunUp == 1,
    isMoonUp = isMoonUp == 1,
    moonPhase = moonPhase,
    moonIllumination = moonIllumination
)

fun CurrentWeatherDto.toEntity(timeEpoch: Long): CurrentWeather = CurrentWeather(
    tempC = tempC,
    feelsLike = feelsLikeC.roundToInt(),
    conditionText = condition.description,
    conditionUrl = condition.iconUrl.correctIconUrl(),
    humidity = humidity.roundToInt(),
    windSpeed = (windKilometersPerHour / 3.6).roundToInt(),
    precipitations = precipitationsMillimeters.roundToInt(),
    snow = snow?.roundToInt(),
    timeEpoch = timeEpoch,
    isDay = isDay == 1,
    conditionCode = condition.code,
    cloud = cloud,
    vision = visionKilometers,
)

fun WeatherForecastDto.toEntity(): Forecast {
    val upcoming = forecastDto.days.map { dayForecast ->
        with (dayForecast.weather) {
            Weather(
                avgTemp = avgTempC,
                conditionCode = condition.code,
                conditionText = condition.description,
                conditionUrl = condition.iconUrl.correctIconUrl(),
                date = dayForecast.date.toCalendar(),
                formattedDate = dayForecast.formattedDate,
                humidity = avgHumidity,
                windSpeed = (maxWindKilometersPerHour / 3.6).roundToInt(),
                snowVolume = totalSnowCm.roundToInt(),
                precipitations = totalPrecipitationsMillimeters.roundToInt(),
                astrologicalParams = dayForecast.astrologicalParams.toEntity(),
                rainChance = rainChance,
                vision = avgVisionKm
            )
        }
    }
    val hourlyWeather = forecastDto.days.map { dayDto ->
        dayDto.hoursWeather.map {
            it.toEntity(it.timeEpoch ?: 0L)
        }
    }
    return Forecast(
        weather = current.toEntity(location.localtimeEpoch),
        upcoming = upcoming,
        hourly = hourlyWeather
    )
}

private fun String.correctIconUrl() = "https:$this".replace("64x64", "128x128")

fun toIconUrl(iconCode: Int) = "https://cdn.weatherapi.com/weather/128x128/day/$iconCode.png"

fun WeatherTypeInfoDto.toEntity(): WeatherTypeInfo {
    val language = Locale.getDefault().language
    val languageDto = languages.find { it.label == language } ?: languages.find { it.label == "en" }
    return WeatherTypeInfo(
        code = code,
        dayDescription = languageDto?.dayText ?: "Undefined",
        nightDescription = languageDto?.nightText ?: "Undefined",
        iconUrl = toIconUrl(iconCode),
    )
}