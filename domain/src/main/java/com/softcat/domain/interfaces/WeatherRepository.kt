package com.softcat.domain.interfaces

import com.softcat.domain.entity.CurrentWeather
import com.softcat.domain.entity.Forecast

interface WeatherRepository {

    suspend fun getWeather(cityId: Int): Result<CurrentWeather>

    suspend fun getForecast(cityId: Int): Result<Forecast>

    suspend fun getTodayLocalForecast(cityId: Int): Result<List<CurrentWeather>>
}