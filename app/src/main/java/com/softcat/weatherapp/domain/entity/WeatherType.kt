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
    Snow,
    Any
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
    WeatherType.Any -> R.drawable.any
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
    WeatherType.Any -> R.string.any
}

fun weatherTypeOf(code: Int) = when (code) {
    1000 -> WeatherType.Sun
    1003, 1006 -> WeatherType.Clouds
    1009 -> WeatherType.HeavyClouds
    1030 -> WeatherType.Mist
    1273, 1276, 1279, 1282 -> WeatherType.Thunderstorm
    1150, 1153, 1168, 1171 -> WeatherType.Drizzle
    1180, 1183, 1186, 1189, 1198, 1201, 1063 -> WeatherType.Rain
    1192, 1195 -> WeatherType.ShowerRain
    1210, 1213, 1216, 1219, 1222, 1225 -> WeatherType.Snow
    else -> WeatherType.Any
}