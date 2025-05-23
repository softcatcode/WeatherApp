package com.softcat.data.implementations

import android.icu.util.Calendar
import com.softcat.data.mapper.toEntity
import com.softcat.data.network.api.DocsApiService
import com.softcat.database.facade.DatabaseFacade
import com.softcat.database.mapper.toDbModel
import com.softcat.database.mapper.toEntity
import com.softcat.domain.entity.CurrentWeather
import com.softcat.domain.entity.Forecast
import com.softcat.domain.entity.Weather
import com.softcat.domain.interfaces.DatabaseLoaderRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.URL
import javax.inject.Inject

class DatabaseLoaderRepositoryImpl @Inject constructor(
    private val database: DatabaseFacade,
    private val docsApiService: DocsApiService,
): DatabaseLoaderRepository {

    private val scope = CoroutineScope(Dispatchers.IO)

    private fun getCurrentDayTime(dayBias: Int): Long {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.MILLISECONDS_IN_DAY, 0)
            add(Calendar.DATE, dayBias)
        }
        return calendar.timeInMillis / 1000L
    }

    override suspend fun tryGetHourlyWeather(
        cityId: Int,
        dayBias: Int
    ): Result<List<CurrentWeather>> {
        val timeEpoch = getCurrentDayTime(dayBias)
        val response = database.getCurrentWeather(cityId, timeEpoch)
        response.onSuccess { hoursWeather ->
            val typeCodes = hoursWeather.map { it.type }
            database.getWeatherTypes(typeCodes).onSuccess { typeModels ->
                val result = hoursWeather.mapIndexed { index, weatherModel ->
                    weatherModel.toEntity(typeModels[index])
                }
                return Result.success(result)
            }.onFailure {
                return Result.failure(it)
            }
        }
        return Result.failure(response.exceptionOrNull()!!)
    }

    override suspend fun tryLoadUpcomingDaysWeather(
        cityId: Int,
        dayCount: Int
    ): Result<List<Weather>> {
        val startTime = getCurrentDayTime(0)
        val endTime = getCurrentDayTime(dayCount)
        val response = database.getDaysWeather(cityId, startTime, endTime)
        response.onSuccess { daysWeather ->
            val typeCodes = daysWeather.map { it.type }
            database.getWeatherTypes(typeCodes).onSuccess { typeModels ->
                val result = daysWeather.mapIndexed { index, weather ->
                    weather.toEntity(typeModels[index])
                }
                return Result.success(result)
            }.onFailure {
                return Result.failure(it)
            }
        }
        return Result.failure(response.exceptionOrNull()!!)
    }

    override fun updateForecastData(cityId: Int, forecast: Forecast) {
        scope.launch {
            forecast.upcoming?.let {
                updateDayWeatherData(cityId, it)
            }
            forecast.hourly.forEach { hourlyWeather ->
                hourlyWeather?.let {
                    updateHourlyWeatherData(cityId, it)
                }
            }
        }
    }

    override suspend fun updateHourlyWeatherData(cityId: Int, hourlyWeather: List<CurrentWeather>) {
        hourlyWeather.forEach {
            val model = it.toDbModel(cityId)
            database.saveCurrentWeather(model)
        }
    }

    override suspend fun updateDayWeatherData(cityId: Int, daysWeather: List<Weather>) {
        daysWeather.forEach {
            val model = it.toDbModel(cityId)
            database.saveWeather(model)
        }
    }

    override suspend fun loadWeatherTypesToDatabase(): Result<Unit> {
        val response = try {
            docsApiService.loadWeatherConditions()
        } catch (e: Exception) {
            return Result.failure(e)
        }
        val weatherTypes = response.map {
            val entity = it.toEntity()
            val url = URL(entity.iconUrl)
            val image = url.readBytes()
            entity.toDbModel(image)
        }
        val result = database.initWeatherTypes(weatherTypes)
        result.onSuccess { return Result.success(it) }
        return Result.failure(result.exceptionOrNull()!!)
    }
}