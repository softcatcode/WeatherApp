package com.softcat.domain.interfaces

import com.softcat.domain.entity.CurrentWeather
import com.softcat.domain.entity.Forecast
import com.softcat.domain.entity.Weather

interface DatabaseLoaderRepository {

    suspend fun tryGetHourlyWeather(cityId: Int, dayBias: Int): Result<List<CurrentWeather>>

    suspend fun tryLoadUpcomingDaysWeather(cityId: Int, dayCount: Int): Result<List<Weather>>

    fun updateForecastData(cityId: Int, forecast: Forecast)

    suspend fun updateHourlyWeatherData(cityId: Int, hourlyWeather: List<CurrentWeather>)

    suspend fun updateDayWeatherData(cityId: Int, daysWeather: List<Weather>)

    suspend fun loadWeatherTypesToDatabase(): Result<Unit>
}