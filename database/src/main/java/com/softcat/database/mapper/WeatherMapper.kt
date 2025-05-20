package com.softcat.database.mapper

import com.softcat.database.model.CurrentWeatherDto
import com.softcat.database.model.WeatherDbModel
import com.softcat.database.model.WeatherTypeDbModel
import com.softcat.domain.entity.AstrologicalParameters
import com.softcat.domain.entity.CurrentWeather
import com.softcat.domain.entity.Weather
import com.softcat.domain.entity.WeatherTypeInfo
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun WeatherTypeInfo.toDbModel() = WeatherTypeDbModel(
    code = code,
    dayDescription = dayDescription,
    nightDescription = nightDescription,
    url = iconUrl,
)

fun Weather.toDbModel(cityId: Int) = WeatherDbModel(
    id = WeatherDbModel.UNSPECIFIED_ID,
    timeEpoch = date.timeInMillis,
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
    isSunUp = astrologicalParams.isSunUp,
    isMoonUp = astrologicalParams.isMoonUp,
    moonPhase = astrologicalParams.moonPhase,
    rainChance = rainChance,
)

fun CurrentWeather.toDbModel(cityId: Int) = CurrentWeatherDto(
    id = CurrentWeatherDto.UNSPECIFIED_ID,
    cityId = cityId,
    timeEpoch = timeEpoch,
    tempC = tempC,
    feelsLike = feelsLike,
    isDay = isDay,
    type = conditionCode,
    windSpeed = windSpeed,
    precipitations = precipitations,
    snow = snow,
    humidity = humidity,
    cloud = cloud,
    vision = vision,
)

fun WeatherTypeDbModel.toEntity() = WeatherTypeInfo(
    code = code,
    dayDescription = dayDescription,
    nightDescription = nightDescription,
    iconUrl = url,
    icon = bytes?.toByteArray() ?: byteArrayOf(),
)

fun CurrentWeatherDto.toEntity(weatherType: WeatherTypeDbModel) = CurrentWeather(
    timeEpoch = timeEpoch,
    tempC = tempC,
    feelsLike = feelsLike,
    isDay = isDay,
    conditionCode = weatherType.code,
    conditionUrl = weatherType.url,
    conditionText = if (isDay) weatherType.dayDescription else weatherType.nightDescription,
    windSpeed = windSpeed,
    precipitations = precipitations,
    snow = snow,
    humidity = humidity,
    cloud = cloud,
    vision = vision,
)

private fun Long.toCalendar() = Calendar.getInstance().apply {
    time = Date(this@toCalendar)
}

private fun Long.toFormattedDate(): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return dateFormat.format(Date(this))
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
        isSunUp = isSunUp,
        isMoonUp = isMoonUp
    ),
    rainChance = rainChance,
)