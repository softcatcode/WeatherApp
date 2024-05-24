package com.softcat.weatherapp.domain.interfaces

import com.softcat.weatherapp.domain.entity.Forecast
import com.softcat.weatherapp.domain.entity.Weather

interface WeatherRepository {

    suspend fun getWeather(cityId: Int): Weather

    suspend fun getForecast(cityId: Int): Forecast
}