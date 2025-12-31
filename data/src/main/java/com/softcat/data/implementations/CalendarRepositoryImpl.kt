package com.softcat.data.implementations

import com.softcat.data.mapper.toEntity
import com.softcat.domain.entity.City
import com.softcat.domain.entity.Weather
import com.softcat.domain.entity.WeatherParameters
import com.softcat.domain.entity.WeatherType
import com.softcat.domain.interfaces.CalendarRepository
import com.softcat.data.network.api.ApiService
import com.softcat.database.facade.DatabaseFacade
import com.softcat.domain.entity.weatherTypeOf
import timber.log.Timber
import java.util.Calendar
import javax.inject.Inject

class CalendarRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val database: DatabaseFacade
): CalendarRepository {

    override suspend fun selectYearDays(
        userId: String,
        params: WeatherParameters,
        city: City,
        selectedYear: Int
    ): Result<List<Set<Int>>> {
        Timber.i("${this::class.simpleName}.selectYearDays($params, $city, $selectedYear)")
        val weatherList = getWeatherForYear(selectedYear, city.id)
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

    private fun getYearStartEpoch(year: Int): Long {
        val millis = Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.DAY_OF_YEAR, 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
        return millis / 1000L
    }

    private suspend fun loadWeatherFromInternet(
        year: Int,
        cityId: Int
    ): List<Weather> {
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        if (year > currentYear)
            return emptyList()

        if (year < currentYear) {
            val forecast = apiService.loadWeatherHistory(
                query = WeatherRepositoryImpl.cityIdToQuery(cityId),
                startDate = "$currentYear-01-01",
                endDate = "$currentYear-12-31"
            ).toEntity()
            return forecast.upcoming ?: emptyList()
        }

        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val first = try {
            apiService.loadWeatherHistory(
                query = WeatherRepositoryImpl.cityIdToQuery(cityId),
                startDate = "$year-01-01",
                endDate = "$year-%02d-%02d".format(month, day)
            ).toEntity().upcoming ?: emptyList()
        } catch (_: Exception) {
            emptyList()
        }

        val second = try {
            apiService.loadForecast(
                query = WeatherRepositoryImpl.cityIdToQuery(cityId),
                dayCount = 10
            ).toEntity().upcoming ?: emptyList()
        } catch (_: Exception) {
            emptyList()
        }

        return first + second
    }

    private suspend fun loadWeatherFromDatabase(year: Int, cityId: Int): List<Weather> {
        val dbWeather = database.getDaysWeather(
            cityId = cityId,
            startSeconds = getYearStartEpoch(year),
            endSeconds = getYearStartEpoch(year + 1) - 1L
        ).getOrNull() ?: emptyList()

        val typeModels = database.getWeatherTypes(
            typeCodes = dbWeather.map { it.type }
        ).getOrNull() ?: return emptyList()

        return dbWeather.mapIndexed { index, item ->
            item.toEntity(typeModels[index])
        }
    }

    private fun mergeWeatherData(first: List<Weather>, second: List<Weather>): List<Weather> {
        val epoch: (Weather) -> Long = { it.date.timeInMillis / 1000L }
        val m = mutableMapOf<Long, Weather>()
        second.forEach {
            m[epoch(it)] = it
        }
        first.forEach {
            m[epoch(it)] = it
        }
        return m.values.toList()
    }

    private suspend fun getWeatherForYear(year: Int, cityId: Int): List<Weather> {
        val dbWeather = loadWeatherFromDatabase(year, cityId)
        val loadedWeather = loadWeatherFromInternet(year, cityId)
        return mergeWeatherData(loadedWeather, dbWeather)
    }
}