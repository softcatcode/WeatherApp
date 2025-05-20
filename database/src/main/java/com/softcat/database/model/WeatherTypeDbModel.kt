package com.softcat.database.model

data class WeatherTypeDbModel(
    val code: Int,
    val dayDescription: String,
    val nightDescription: String,
    val url: String,
    val bytes: String? = null,
)