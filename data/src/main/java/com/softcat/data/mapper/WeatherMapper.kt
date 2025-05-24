package com.softcat.data.mapper

import com.softcat.data.network.dto.AstronomicParametersDto
import com.softcat.data.network.dto.CurrentWeatherResponse
import com.softcat.data.network.dto.CurrentWeatherDto
import com.softcat.data.network.dto.WeatherForecastDto
import com.softcat.data.network.dto.WeatherTypeInfoDto
import com.softcat.database.model.CurrentWeatherDbModel
import com.softcat.database.model.WeatherDbModel
import com.softcat.database.model.WeatherTypeDbModel
import com.softcat.domain.entity.AstrologicalParameters
import com.softcat.domain.entity.CurrentWeather
import com.softcat.domain.entity.Forecast
import com.softcat.domain.entity.Weather
import com.softcat.domain.entity.WeatherTypeInfo
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

fun CurrentWeatherResponse.toEntity() = value.toEntity(location.localtimeEpoch)

private fun String.hour12ToTimeEpoch(): Long {
    val i = indexOfFirst { it == ':' }
    val j = indexOfFirst { it == ' ' }
    val minute = substring(i + 1, j).toInt()
    val label = substring(j + 1)
    val hour = substring(0, i).toInt() + if (label == "PM") 12 else 0

    val time = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, hour)
        set(Calendar.MINUTE, minute)
        set(Calendar.SECOND, 0)
    }.timeInMillis / 1000L

    return time
}

fun AstronomicParametersDto.toEntity() = AstrologicalParameters(
    sunriseTime = sunriseTime.hour12ToTimeEpoch(),
    sunsetTime = sunsetTime.hour12ToTimeEpoch(),
    moonriseTime = moonriseTime.hour12ToTimeEpoch(),
    moonsetTime = moonsetTime.hour12ToTimeEpoch(),
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

fun WeatherTypeInfo.toDbModel(bytes: ByteArray?) = WeatherTypeDbModel(
    code = code,
    dayDescription = dayDescription,
    nightDescription = nightDescription,
    url = iconUrl,
    iconBytes = bytes
)

fun Weather.toDbModel(cityId: Int) = WeatherDbModel(
    id = WeatherDbModel.UNSPECIFIED_ID,
    timeEpoch = date.timeInMillis / 1000L,
    cityId = cityId,
    type = conditionCode,
    avgTemp = avgTemp,
    humidity = humidity,
    windSpeed = windSpeed,
    snowVolume = snowVolume,
    precipitations = precipitations,
    vision = vision,
    sunriseTime = astrologicalParams.sunriseTime,
    sunsetTime = astrologicalParams.sunsetTime,
    moonriseTime = astrologicalParams.moonriseTime,
    moonsetTime = astrologicalParams.moonsetTime,
    moonIllumination = astrologicalParams.moonIllumination,
    moonPhase = astrologicalParams.moonPhase,
    rainChance = rainChance,
)

fun CurrentWeather.toDbModel(cityId: Int) = CurrentWeatherDbModel(
    id = CurrentWeatherDbModel.UNSPECIFIED_ID,
    cityId = cityId,
    timeEpoch = timeEpoch,
    tempC = tempC,
    feelsLike = feelsLike,
    isDay = if (isDay) 1 else 0,
    type = conditionCode,
    windSpeed = windSpeed,
    precipitations = precipitations,
    snow = snow,
    humidity = humidity,
    cloud = cloud,
    vision = vision,
)

fun CurrentWeatherDbModel.toEntity(weatherType: WeatherTypeDbModel) = CurrentWeather(
    timeEpoch = timeEpoch,
    tempC = tempC,
    feelsLike = feelsLike,
    isDay = isDay == 1,
    conditionCode = weatherType.code,
    conditionUrl = weatherType.url,
    conditionText = if (isDay == 1) weatherType.dayDescription else weatherType.nightDescription,
    windSpeed = windSpeed,
    precipitations = precipitations,
    snow = snow,
    humidity = humidity,
    cloud = cloud,
    vision = vision,
)

fun Long.toCalendar() = Calendar.getInstance().apply {
    time = Date(this@toCalendar * 1000)
}

private fun Long.toFormattedDate(): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return dateFormat.format(Date(this * 1000))
}

fun WeatherDbModel.toEntity(weatherType: WeatherTypeDbModel) = Weather(
    avgTemp = avgTemp,
    conditionCode = weatherType.code,
    conditionText = weatherType.dayDescription,
    conditionUrl = weatherType.url,
    date = timeEpoch.toCalendar(),
    formattedDate = timeEpoch.toFormattedDate(),
    vision = vision,
    humidity = humidity,
    windSpeed = windSpeed,
    snowVolume = snowVolume,
    precipitations = precipitations,
    astrologicalParams = AstrologicalParameters(
        sunriseTime = sunriseTime,
        sunsetTime = sunsetTime,
        moonriseTime = moonriseTime,
        moonsetTime = moonsetTime,
        moonPhase = moonPhase,
        moonIllumination = moonIllumination,
    ),
    rainChance = rainChance,
)