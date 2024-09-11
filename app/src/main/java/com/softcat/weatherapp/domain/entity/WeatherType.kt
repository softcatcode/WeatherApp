package com.softcat.weatherapp.domain.entity

import com.softcat.weatherapp.R

enum class WeatherType {
    Thunderstorm,
    Drizzle,
    Rain,
    ShowerRain,
    Mist,
    Sun,
    Clouds,
    HeavyClouds,
    Snow
}

fun WeatherType.toIconResId() = when (this) {
    WeatherType.Thunderstorm -> R.drawable.thunderstorm
    WeatherType.Drizzle -> R.drawable.drizzle
    WeatherType.Rain -> R.drawable.rain
    WeatherType.ShowerRain -> R.drawable.shower
    WeatherType.Mist -> R.drawable.mist
    WeatherType.Sun -> R.drawable.sun
    WeatherType.Clouds -> R.drawable.clouds
    WeatherType.HeavyClouds -> R.drawable.heavy_clouds
    WeatherType.Snow -> R.drawable.snow
}

fun WeatherType.toTitleResId() = when (this) {
    WeatherType.Thunderstorm -> R.string.thunderstorm
    WeatherType.Drizzle -> R.string.drizzle
    WeatherType.Rain -> R.string.rain
    WeatherType.ShowerRain -> R.string.shower
    WeatherType.Mist -> R.string.mist
    WeatherType.Sun -> R.string.sun
    WeatherType.Clouds -> R.string.clouds
    WeatherType.HeavyClouds -> R.string.heavy_clouds
    WeatherType.Snow -> R.string.snow
}