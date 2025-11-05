package com.softcat.data.implementations.new_api

import com.softcat.data.mapper.toEntity
import com.softcat.domain.interfaces.WeatherRepository
import com.softcat.data.network.api.ApiService
import com.softcat.data.network.api.NewWeatherApiService
import com.softcat.domain.entity.CurrentWeather
import com.softcat.domain.entity.Forecast
import timber.log.Timber
import java.util.Calendar
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val apiService: NewWeatherApiService,
): WeatherRepository {

    override suspend fun getWeather(userId: String, cityId: Int): Result<CurrentWeather> {
        Timber.i("${this::class.simpleName}.getWeather($userId, $cityId)")
        return try {
            val result = apiService.loadHoursWeather(cityId, userId).toEntity()
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getForecast(userId: String, cityId: Int): Result<Forecast> {
        Timber.i("${this::class.simpleName}.getForecast($userId, $cityId)")
        return try {
            val epoch = getCurrentDayEpoch()
            val forecast = apiService.loadForecast(
                userId = userId,
                cityId = cityId,
                startEpoch = epoch
            ).toEntity()
            Result.success(forecast)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getTodayLocalForecast(userId: String, cityId: Int): Result<List<CurrentWeather>> {
        Timber.i("${this::class.simpleName}.getTodayLocalForecast($userId, $cityId)")
        return try {
            val epoch = getCurrentDayEpoch()
            val forecast = apiService.loadForecast(
                userId = userId,
                cityId = cityId,
                startEpoch = epoch,
                endEpoch = epoch + 1L,
            ).toEntity()
            val hourlyWeather = forecast.hourly.first()
            hourlyWeather?.let {
                Result.success(it)
            } ?: Result.failure(NullPointerException())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun getCurrentDayEpoch(): Long {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return calendar.timeInMillis / 1000L
    }

    companion object {
        private const val PREFIX_CITY_ID = "id:"

        fun cityIdToQuery(cityId: Int) = "$PREFIX_CITY_ID$cityId"
    }
}