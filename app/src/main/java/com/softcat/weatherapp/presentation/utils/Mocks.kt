package com.softcat.weatherapp.presentation.utils

import com.softcat.domain.entity.CurrentWeather

val defaultWeather = CurrentWeather(
    tempC = 20f,
    feelsLike = 23,
    conditionText = "Overcast Clouds with a bit of sun",
    conditionUrl = "//cdn.weatherapi.com/weather/64x64/night/113.png",
    humidity = 20,
    windSpeed = 3,
    precipitations = 2,
    conditionCode = 1006,
    vision = 10f,
    timeEpoch = 516279846,
    isDay = true,
    cloud = 70,
    snow = 0,
)