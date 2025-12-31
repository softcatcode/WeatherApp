package com.softcat.data.implementations.new_api

import android.icu.util.Calendar
import com.softcat.data.mapper.toEntity
import com.softcat.data.network.api.NewWeatherApiService
import com.softcat.database.facade.DatabaseFacade
import com.softcat.domain.entity.City
import com.softcat.domain.entity.Weather
import com.softcat.domain.entity.WeatherParameters
import com.softcat.domain.entity.WeatherType
import com.softcat.domain.entity.weatherTypeOf
import com.softcat.domain.interfaces.CalendarRepository
import timber.log.Timber
import javax.inject.Inject

class CalendarRepositoryImpl @Inject constructor(
    private val apiService: NewWeatherApiService,
    private val database: DatabaseFacade
): CalendarRepository {
    override suspend fun selectYearDays(
        userId: String,
        params: WeatherParameters,
        city: City,
        selectedYear: Int
    ): Result<List<Set<Int>>> {
        Timber.i("${this::class.simpleName}.selectYearDays($params, $city, $selectedYear)")
        val weatherList = getWeatherForYear(userId, city.id, selectedYear)
        val monthsDays = List<MutableSet<Int>>(12) { mutableSetOf() }

        weatherList.filter { weather ->
            matches(weather, params)
        }.forEach { weather ->
            val month = weather.date.get(Calendar.MONTH)
            val day = weather.date.get(Calendar.DAY_OF_MONTH)
            monthsDays[month].add(day)
        }
        return Result.success(monthsDays)
    }

    private fun matches(weather: Weather, params: WeatherParameters): Boolean {
        val type = weatherTypeOf(weather.conditionCode)
        return (params.weatherType == WeatherType.Any || params.weatherType == type) &&
                weather.avgTemp in params.temperature &&
                weather.humidity in params.humidity &&
                weather.precipitations in params.precipitations &&
                weather.windSpeed in params.windSpeed &&
                weather.snowVolume in params.snowVolume
    }

    private fun getYearEpoch(year: Int) = Calendar.getInstance().apply {
        set(Calendar.YEAR, year)
        set(Calendar.DAY_OF_YEAR, 0)
        set(Calendar.MILLISECONDS_IN_DAY, 0)
    }.timeInMillis / 1000

    private suspend fun getWeatherForYear(userId: String, cityId: Int, year: Int,): List<Weather> {
        val start = getYearEpoch(year)
        val end = getYearEpoch(year + 1)
        return try {
            val forecast = apiService.loadForecast(cityId, userId, start, end)
            val weather = forecast.toEntity().upcoming ?: emptyList()
            weather
        } catch (_: Exception) {
            emptyList()
        }
    }
}