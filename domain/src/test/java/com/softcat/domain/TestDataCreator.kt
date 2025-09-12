package com.softcat.domain

import com.softcat.domain.entity.AstrologicalParameters
import com.softcat.domain.entity.CurrentWeather
import com.softcat.domain.entity.Forecast
import com.softcat.domain.entity.User
import com.softcat.domain.entity.Weather
import java.util.Calendar
import java.util.Date
import kotlin.random.Random

object TestDataCreator {

    fun getTestCurrentWeatherList(): List<CurrentWeather> {
        return List(24) { i ->
            CurrentWeather(
                timeEpoch = 1757538000L + i * 3600L,
                tempC = Random.nextInt(18, 25).toFloat(),
                feelsLike = Random.nextInt(17, 27),
                isDay = i in 6..21,
                conditionCode = 100,
                conditionUrl = "https://sunny_condition.png",
                conditionText = "Sunny and no clouds",
                windSpeed = Random.nextInt(1, 3),
                precipitations = Random.nextInt(0, 1),
                snow = 0,
                humidity = Random.nextInt(15, 25),
                cloud = Random.nextInt(5, 30),
                vision = 1f
            )
        }
    }

    fun getTestWeather() = Weather(
        avgTemp = 20f,
        conditionCode = 200,
        conditionText = "Cloudy",
        conditionUrl = "https://cloudy_condition.png",
        date = Calendar.getInstance().apply { time = Date(1757488236000L) },
        formattedDate = "10.09.2025",
        vision = Random.nextInt(3, 10).toFloat() / 10f,
        humidity = Random.nextInt(5, 20),
        windSpeed = Random.nextInt(0, 5),
        snowVolume = 0,
        precipitations = Random.nextInt(0, 5),
        astrologicalParams = AstrologicalParameters(
            sunriseTime = 1757473200,
            sunsetTime = 1757529000,
            moonriseTime = 1757547000,
            moonsetTime = 1757553720,
            moonPhase = "Full",
            moonIllumination = Random.nextInt(20, 70)
        ),
        rainChance = 20
    )

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
        hourly = List(3) { getTestCurrentWeatherList() },
        upcoming = List(3) { getTestWeather() }
    )

    fun getRandomLogin(): String {
        val name = listOf("Billy", "Lisa", "Mark", "Bob")[Random.nextInt(0, 4)]
        val num = Random.nextInt(100, 10000)
        return "$name-$num"
    }

    fun getRandomEmail(): String {
        val name = listOf("Billy", "Lisa", "Mark", "Bob")[Random.nextInt(0, 4)]
        val service = listOf("gmail", "mail", "yandex", "google")[Random.nextInt(0, 4)]
        val end = listOf("ru", "com", "org")[Random.nextInt(0, 3)]
        return "$name@$service.$end"
    }

    fun getRandomPassword() = Random.nextInt(10000, 100000).toString()

    fun getTestUser() = User(
        id = "0495128357",
        name = "Bill",
        role = User.Status.Premium,
        email = "billy@gmail.com",
        password = "1_Pw90fVC"
    )
}