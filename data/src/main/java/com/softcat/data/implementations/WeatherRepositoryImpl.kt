package com.softcat.data.implementations

import com.softcat.data.mapper.toEntity
import com.softcat.domain.interfaces.WeatherRepository
import com.softcat.data.network.api.ApiService
import com.softcat.domain.entity.CurrentWeather
import com.softcat.domain.entity.Forecast
import timber.log.Timber
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
): WeatherRepository {

    override suspend fun getWeather(userId: String, cityId: Int): Result<CurrentWeather> {
        Timber.i("${this::class.simpleName}.getWeather($cityId)")
        return try {
            val result = apiService.loadCurrentWeather(cityIdToQuery(cityId)).toEntity()
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getForecast(userId: String, cityId: Int): Result<Forecast> {
        Timber.i("${this::class.simpleName}.getForecast($cityId)")
        return try {
            val forecast = apiService.loadForecast(cityIdToQuery(cityId)).toEntity()
            Result.success(forecast)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getTodayLocalForecast(userId: String, cityId: Int): Result<List<CurrentWeather>> {
        Timber.i("${this::class.simpleName}.getTodayLocalForecast($cityId)")
        return try {
            val forecast = apiService.loadForecast(cityIdToQuery(cityId), 1).toEntity()
            val hourlyWeather = forecast.hourly.first()
            hourlyWeather?.let {
                Result.success(it)
            } ?: Result.failure(NullPointerException())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    companion object {
        private const val PREFIX_CITY_ID = "id:"

        fun cityIdToQuery(cityId: Int) = "$PREFIX_CITY_ID$cityId"
    }
}