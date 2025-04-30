package com.softcat.weatherapp.presentation.utils

import com.softcat.domain.entity.AstrologicalParameters
import com.softcat.domain.entity.Weather
import com.softcat.domain.entity.WeatherType
import java.util.Calendar

val defaultWeather = Weather(
    type = WeatherType.Clouds,
    tempC = 20f,
    feelsLike = 23f,
    conditionText = "Overcast Clouds with a bit of sun",
    conditionUrl = "//cdn.weatherapi.com/weather/64x64/night/113.png",
    date = Calendar.getInstance(),
    formattedDate = "2024-09-29",
    humidity = 20f,
    windSpeed = 3f,
    snowVolume = 0f,
    precipitations = 2f,
    astrologicalParams = AstrologicalParameters(
        sunriseTime = "06:12 AM",
        sunsetTime = "21:32 PM",
        moonriseTime = "22:01 AM",
        moonsetTime = "05:05 AM",
    )
)