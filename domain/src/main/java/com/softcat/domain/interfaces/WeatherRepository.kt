package com.softcat.domain.interfaces

import com.softcat.domain.entity.Forecast
import com.softcat.domain.entity.Weather

interface WeatherRepository {

    suspend fun getWeather(cityId: Int): Weather

    suspend fun getForecast(cityId: Int): Forecast

    suspend fun getTodayLocalForecast(cityId: Int): List<Weather>
}