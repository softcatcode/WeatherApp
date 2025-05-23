package com.softcat.data.implementations

import com.softcat.data.mapper.toEntity
import com.softcat.domain.entity.City
import com.softcat.domain.entity.Weather
import com.softcat.domain.entity.WeatherParameters
import com.softcat.domain.entity.WeatherType
import com.softcat.domain.interfaces.CalendarRepository
import com.softcat.data.network.api.ApiService
import com.softcat.domain.entity.weatherTypeOf
import timber.log.Timber
import java.util.Calendar
import javax.inject.Inject

class CalendarRepositoryImpl @Inject constructor(
    private val apiService: ApiService
): CalendarRepository {

    override suspend fun selectYearDays(
        params: WeatherParameters,
        city: City,
        selectedYear: Int
    ): Result<List<Set<Int>>> {
        Timber.i("${this::class.simpleName}.selectYearDays($params, $city, $selectedYear)")
        val nextWeatherList: List<Weather>
        val prevWeatherList: List<Weather>
        try {
            val currentYear = Calendar.getInstance().get(Calendar.YEAR)
            nextWeatherList = if (currentYear == selectedYear)
                getWeatherForecast(10, city.id)
            else
                emptyList()
            prevWeatherList = if (currentYear > selectedYear)
                getPreviousWeather(selectedYear, city.id)
            else
                emptyList()
        } catch (e: Exception) {
            return Result.failure(e)
        }
        val monthsDays = List<MutableSet<Int>>(12) { mutableSetOf() }

        (nextWeatherList + prevWeatherList).filter { weather ->
            val type = weatherTypeOf(weather.conditionCode)
            (params.weatherType == WeatherType.Any || params.weatherType == type) &&
            weather.avgTemp in params.temperature &&
            weather.humidity in params.humidity &&
            weather.precipitations in params.precipitations &&
            weather.windSpeed in params.windSpeed &&
            weather.snowVolume in params.snowVolume
        }.forEach { weather ->
            val date = weather.formattedDate
            val year = date.substringBefore('-', "0").toInt()
            if (year == selectedYear) {
                val month = date.substring(
                    date.indexOfFirst { it == '-' } + 1,
                    date.indexOfLast { it == '-' }
                ).toInt()
                val day = date.substring(date.indexOfLast { it == '-' } + 1).toInt()
                monthsDays[month - 1].add(day)
            }
        }
        return Result.success(monthsDays)
    }

    private suspend fun getWeatherForecast(daysCount: Int, cityId: Int): List<Weather> {
        Timber.i("${this::class.simpleName}.getWeatherForecast($daysCount, $cityId)")
        val forecast = apiService.loadForecast(
            query = WeatherRepositoryImpl.cityIdToQuery(cityId),
            dayCount = daysCount
        ).toEntity()
        return forecast.upcoming!!
    }

    private suspend fun getPreviousWeather(currentYear: Int, cityId: Int): List<Weather> {
        Timber.i("${this::class.simpleName}.getPreviousWeather($currentYear, $cityId)")
        val forecast = apiService.loadWeatherHistory(
            query = WeatherRepositoryImpl.cityIdToQuery(cityId),
            startDate = "$currentYear-01-01",
            endDate = "$currentYear-12-31"
        ).toEntity()
        return forecast.upcoming!!
    }
}