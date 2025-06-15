package com.softcat.data.implementations

import android.icu.util.Calendar
import com.softcat.data.mapper.toDbModel
import com.softcat.data.mapper.toEntities
import com.softcat.data.mapper.toEntity
import com.softcat.data.network.api.DocsApiService
import com.softcat.database.facade.DatabaseFacade
import com.softcat.domain.entity.City
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

    override suspend fun searchCities(query: String): Result<List<City>> {
        return try {
            val cityList = database.searchCity(query).getOrThrow()
            val countryList = database.getCountries().getOrThrow()
            val result = cityList.toEntities(countryList)
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun tryGetHourlyWeather(
        cityId: Int,
        dayBias: Int
    ): Result<List<CurrentWeather>> {
        val timeEpoch = getCurrentDayTime(dayBias)
        return try {
            val hoursWeather = database.getCurrentWeather(cityId, timeEpoch).getOrThrow()
            val typeCodes = hoursWeather.map { it.type }
            val typeModels = database.getWeatherTypes(typeCodes).getOrThrow()
            val result = hoursWeather.mapIndexed { index, weatherModel ->
                weatherModel.toEntity(typeModels[index])
            }
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun tryLoadUpcomingDaysWeather(
        cityId: Int,
        dayCount: Int
    ): Result<List<Weather>> {
        val startTime = getCurrentDayTime(0)
        val endTime = getCurrentDayTime(dayCount)
        return try {
            val daysWeather = database.getDaysWeather(cityId, startTime, endTime).getOrThrow()
            val typeCodes = daysWeather.map { it.type }
            val typeModels = database.getWeatherTypes(typeCodes).getOrThrow()
            val result = daysWeather.mapIndexed { index, weather ->
                weather.toEntity(typeModels[index])
            }
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
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
        return try {
            val result = database.initWeatherTypes(weatherTypes).getOrThrow()
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}