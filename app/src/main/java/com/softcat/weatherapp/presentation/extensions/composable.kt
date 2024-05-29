package com.softcat.weatherapp.presentation.extensions

import kotlin.math.roundToInt

fun Float.toTemperatureString() = "${roundToInt()}°C"