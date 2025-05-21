package com.softcat.data.implementations

import com.softcat.data.mapper.toEntity
import com.softcat.domain.interfaces.WeatherRepository
import com.softcat.data.network.api.ApiService
import com.softcat.data.network.api.DocsApiService
import com.softcat.database.facade.DatabaseFacade
import com.softcat.database.mapper.toDbModel
import com.softcat.domain.entity.CurrentWeather
import com.softcat.domain.entity.Forecast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.net.URL
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val docsApiService: DocsApiService,
    private val database: DatabaseFacade
): WeatherRepository {

    private val scope = CoroutineScope(Dispatchers.IO)

    override suspend fun getWeather(cityId: Int): CurrentWeather {
        Timber.i("${this::class.simpleName}.getWeather($cityId)")
        return apiService.loadCurrentWeather(cityIdToQuery(cityId)).toEntity()
    }

    override suspend fun getForecast(cityId: Int): Forecast {
        Timber.i("${this::class.simpleName}.getForecast($cityId)")
        val forecast = apiService.loadForecast(cityIdToQuery(cityId)).toEntity()
        updateDatabaseContent(cityId, forecast)
        return forecast
    }

    override suspend fun getTodayLocalForecast(cityId: Int): List<CurrentWeather> {
        Timber.i("${this::class.simpleName}.getTodayLocalForecast($cityId)")
        val forecast = apiService.loadForecast(cityIdToQuery(cityId), 1).toEntity()
        updateDatabaseContent(cityId, forecast)
        return forecast.hourly.first()
    }

    override suspend fun loadWeatherTypesToDatabase() {
        val response = try {
            docsApiService.loadWeatherConditions()
        } catch (e: Exception) {
            return
        }
        val weatherTypes = response.map {
            val entity = it.toEntity()
            val url = URL(entity.iconUrl)
            val image = url.readBytes()
            entity.toDbModel(image)
        }
        database.initWeatherTypes(weatherTypes)
    }

    @Synchronized
    private fun updateDatabaseContent(cityId: Int, forecast: Forecast) {
        scope.launch {
            forecast.upcoming.forEach {
                val model = it.toDbModel(cityId)
                database.saveWeather(model)
            }
            forecast.hourly.forEach { dayHourlyWeather ->
                dayHourlyWeather.forEach {
                    val model = it.toDbModel(cityId)
                    database.saveCurrentWeather(model)
                }
            }
        }
    }

    companion object {
        private const val PREFIX_CITY_ID = "id:"

        fun cityIdToQuery(cityId: Int) = "$PREFIX_CITY_ID$cityId"
    }
}