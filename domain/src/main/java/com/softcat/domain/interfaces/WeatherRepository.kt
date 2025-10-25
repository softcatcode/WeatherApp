package com.softcat.domain.interfaces

import com.softcat.domain.entity.CurrentWeather
import com.softcat.domain.entity.Forecast

interface WeatherRepository {

    suspend fun getWeather(userId: String, cityId: Int): Result<CurrentWeather>

    suspend fun getForecast(userId: String, cityId: Int): Result<Forecast>

    suspend fun getTodayLocalForecast(userId: String, cityId: Int): Result<List<CurrentWeather>>
}