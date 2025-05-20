package com.softcat.domain.interfaces

import com.softcat.domain.entity.CurrentWeather
import com.softcat.domain.entity.Forecast

interface WeatherRepository {

    suspend fun getWeather(cityId: Int): CurrentWeather

    suspend fun getForecast(cityId: Int): Forecast

    suspend fun getTodayLocalForecast(cityId: Int): List<CurrentWeather>
}