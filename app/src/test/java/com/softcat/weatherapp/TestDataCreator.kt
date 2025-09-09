package com.softcat.weatherapp

import com.softcat.data.network.dto.CityDto
import com.softcat.data.network.dto.ConditionDto
import com.softcat.data.network.dto.WeatherTypeInfoDto
import com.softcat.domain.entity.AstrologicalParameters
import com.softcat.domain.entity.City
import com.softcat.domain.entity.CurrentWeather
import com.softcat.domain.entity.Forecast
import com.softcat.domain.entity.User
import com.softcat.domain.entity.Weather
import java.util.Calendar
import java.util.Date
import kotlin.random.Random

object TestDataCreator {

    fun getCityName() = "London"

    fun getTestUser() = User(
        id = "0495128357",
        name = "Bill",
        role = User.Status.Premium,
        email = "billy@gmail.com",
        password = "1_Pw90fVC"
    )

    fun getUserId() = "user-38517-ttt"

    fun getTestCity() = City(
        id = 1852,
        name = "London",
        country = "England",
        latitude = 5.26f,
        longitude = -14f
    )

    fun getCityList() = listOf(
        City(
            id = 1852,
            name = "Moscow",
            country = "England",
            latitude = 5.26f,
            longitude = -14f
        ),
        City(
            id = 1852,
            name = "London",
            country = "England",
            latitude = -3.51f,
            longitude = 0f
        ),
        City(
            id = 1852,
            name = "Amsterdam",
            country = "Netherlands",
            latitude = 0.236f,
            longitude = 80.5f
        )
    )

    fun getCityDtoList() = listOf(
        CityDto(
            id = 1852,
            name = "Moscow",
            country = "England",
            latitude = 5.26f,
            longitude = -14f,
            region = "unknown",
            url = "https://weather_api/cities/Moscow",
        ),
        CityDto(
            id = 1852,
            name = "London",
            country = "England",
            latitude = -3.51f,
            longitude = 0f,
            region = "unknown",
            url = "https://weather_api/cities/Moscow",
        ),
        CityDto(
            id = 1852,
            name = "Amsterdam",
            country = "Netherlands",
            latitude = 0.236f,
            longitude = 80.5f,
            region = "unknown",
            url = "https://weather_api/cities/Moscow",
        )
    )

    fun getCityQuery() = "random-query-${Random.nextInt(1000, 10000)}"

    fun getTestForecast() = Forecast(
        weather = CurrentWeather(
            timeEpoch = 1757412500,
            tempC = 20f,
            feelsLike = 18,
            isDay = true,
            conditionCode = 100,
            conditionUrl = "https://sunny_condition.png",
            conditionText = "Sunny and no clouds",
            windSpeed = 1,
            precipitations = 0,
            snow = 0,
            humidity = 20,
            cloud = 15,
            vision = 1f
        ),
        hourly = listOf(
            listOf(
                CurrentWeather(
                    timeEpoch = 1757412500,
                    tempC = 22f,
                    feelsLike = 22,
                    isDay = true,
                    conditionCode = 100,
                    conditionUrl = "https://sunny_condition.png",
                    conditionText = "Sunny and no clouds",
                    windSpeed = 1,
                    precipitations = 0,
                    snow = 0,
                    humidity = 20,
                    cloud = 10,
                    vision = 1f
                ),
                CurrentWeather(
                    timeEpoch = 1757412500,
                    tempC = 25f,
                    feelsLike = 22,
                    isDay = true,
                    conditionCode = 100,
                    conditionUrl = "https://sunny_condition.png",
                    conditionText = "Sunny and no clouds",
                    windSpeed = 1,
                    precipitations = 0,
                    snow = 0,
                    humidity = 20,
                    cloud = 5,
                    vision = 1f
                )
            )
        ),
        upcoming = listOf(
            Weather(
                avgTemp = 20f,
                conditionCode = 200,
                conditionText = "Cloudy",
                conditionUrl = "https://cloudy_condition.png",
                date = Calendar.getInstance().apply { time = Date(1757488236000L) },
                formattedDate = "10.09.2025",
                vision = 0.6f,
                humidity = 30,
                windSpeed = 3,
                snowVolume = 0,
                precipitations = 0,
                astrologicalParams = AstrologicalParameters(
                    sunriseTime = 1757473200,
                    sunsetTime = 1757529000,
                    moonriseTime = 1757547000,
                    moonsetTime = 1757553720,
                    moonPhase = "Full",
                    moonIllumination = 60
                ),
                rainChance = 20
            )
        )
    )

    fun getTestConditions() = listOf(
        WeatherTypeInfoDto(
            code = 100,
            day = "sunny",
            night = "clearing",
            iconCode = 10,
            languages = emptyList()
        ),
        WeatherTypeInfoDto(
            code = 101,
            day = "snowy",
            night = "snowy",
            iconCode = 11,
            languages = emptyList()
        ),
        WeatherTypeInfoDto(
            code = 102,
            day = "thunderstorm",
            night = "rain and lightning",
            iconCode = 12,
            languages = emptyList()
        ),
        WeatherTypeInfoDto(
            code = 103,
            day = "rainy, day",
            night = "rainy, night",
            iconCode = 13,
            languages = emptyList()
        ),
    )
}