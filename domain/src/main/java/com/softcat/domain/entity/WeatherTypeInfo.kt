package com.softcat.domain.entity

data class WeatherTypeInfo(
    val code: Int,
    val dayDescription: String,
    val nightDescription: String,
    val iconUrl: String,
)