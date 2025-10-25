package com.softcat.domain.useCases

import com.softcat.domain.entity.Forecast
import com.softcat.domain.interfaces.DatabaseLoaderRepository
import com.softcat.domain.interfaces.WeatherRepository
import timber.log.Timber
import javax.inject.Inject

class GetForecastUseCase @Inject constructor(
    private val repository: WeatherRepository,
    private val databaseRepository: DatabaseLoaderRepository
) {
    suspend operator fun invoke(userId: String, cityId: Int, dayCount: Int = 2): Forecast {
        Timber.i("${this::class.simpleName} invoked")
        val apiResponse = repository.getForecast(userId, cityId)
        apiResponse.onSuccess {
            databaseRepository.updateForecastData(cityId, it)
            return it
        }
        return loadForecastFromDatabase(cityId, dayCount)
    }

    suspend fun loadForecastFromDatabase(cityId: Int, dayCount: Int): Forecast {
        var error: Throwable? = null
        val hourlyWeather = List(dayCount) { dayIndex ->
            val response = databaseRepository.tryGetHourlyWeather(cityId, dayIndex)
            response.onSuccess { return@List it }
            error = response.exceptionOrNull()
            null
        }
        val daysWeather = databaseRepository.tryLoadUpcomingDaysWeather(cityId, dayCount)
            .let { result ->
                result.onFailure { error = it }
                result.getOrNull()
            }

        if (daysWeather == null && hourlyWeather.find { it != null } == null)
            throw error ?: NullPointerException()
        return Forecast(
            weather = null,
            hourly = hourlyWeather,
            upcoming = daysWeather,
        )
    }
}