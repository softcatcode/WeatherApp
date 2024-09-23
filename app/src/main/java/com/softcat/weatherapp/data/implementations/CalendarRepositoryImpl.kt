package com.softcat.weatherapp.data.implementations

import com.softcat.weatherapp.data.mapper.toEntity
import com.softcat.weatherapp.data.network.api.ApiService
import com.softcat.weatherapp.domain.entity.City
import com.softcat.weatherapp.domain.entity.Weather
import com.softcat.weatherapp.domain.entity.WeatherParameters
import com.softcat.weatherapp.domain.entity.WeatherType
import com.softcat.weatherapp.domain.interfaces.CalendarRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.shareIn
import javax.inject.Inject

class CalendarRepositoryImpl @Inject constructor(
    private val apiService: ApiService
): CalendarRepository {

    private data class GetDaysRequest(
        val params: WeatherParameters,
        val city: City,
        val selectedYear: Int
    )

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    private val getHighlightedDaysRequest = MutableSharedFlow<GetDaysRequest?>(replay = 1)
    private val highlightedDaysFlow = flow {
        getHighlightedDaysRequest.emit(null)
        getHighlightedDaysRequest.collect { request ->
            if (request == null) {
                _monthsDays = List(12) { mutableSetOf() }
            } else with (request) {
                loadDaysWithCorrespondingWeather(params, city, selectedYear)
            }
            emit(monthsDays)
        }
    }.shareIn(
        scope = coroutineScope,
        started = SharingStarted.Eagerly,
        replay = 1
    )

    private var _monthsDays: List<MutableSet<Int>> = List(12) { mutableSetOf() }
    private val monthsDays: List<Set<Int>>
        get() = List(12) { _monthsDays[it] }

    override suspend fun selectYearDays(
        params: WeatherParameters,
        city: City,
        selectedYear: Int
    ) {
        getHighlightedDaysRequest.emit(
            GetDaysRequest(params, city, selectedYear)
        )
    }

    override fun getHighlightedDays() = highlightedDaysFlow
    override suspend fun reset() {
        getHighlightedDaysRequest.emit(null)
    }

    private suspend fun getWeatherForecast(daysCount: Int, cityId: Int): List<Weather> {
        return try {
            apiService.loadForecast(
                query = WeatherRepositoryImpl.cityIdToQuery(cityId),
                dayCount = daysCount
            ).toEntity(dropCurrentForecast = false).upcoming
        } catch (e: Exception) {
            emptyList()
        }
    }

    private suspend fun getPreviousWeather(currentYear: Int, cityId: Int): List<Weather> {
        return try {
            apiService.loadWeatherHistory(
                query = WeatherRepositoryImpl.cityIdToQuery(cityId),
                startDate = "$currentYear-01-01",
                endDate = "$currentYear-12-31"
            ).toEntity().upcoming
        } catch (e: Exception) {
            emptyList()
        }
    }

    private suspend fun loadDaysWithCorrespondingWeather(
        params: WeatherParameters,
        city: City,
        selectedYear: Int
    ) {
        val nextWeatherList = getWeatherForecast(10, city.id)
        val prevWeatherList = getPreviousWeather(selectedYear, city.id)
        _monthsDays = List(12) { mutableSetOf() }

        (nextWeatherList + prevWeatherList).filter { weather ->
            (params.weatherType == WeatherType.Any || weather.type == params.weatherType) &&
            weather.tempC in params.temperature &&
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
                _monthsDays[month - 1].add(day)
            }
        }
    }
}