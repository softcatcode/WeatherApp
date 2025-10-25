package com.softcat.domain.useCases

import com.softcat.domain.entity.CurrentWeather
import com.softcat.domain.interfaces.DatabaseLoaderRepository
import com.softcat.domain.interfaces.WeatherRepository
import timber.log.Timber
import javax.inject.Inject

class GetTodayForecastUseCase @Inject constructor(
    private val repository: WeatherRepository,
    private val databaseRepository: DatabaseLoaderRepository
) {
    suspend operator fun invoke(userId: String, cityId: Int): List<CurrentWeather> {
        Timber.i("${this::class.simpleName} invoked")
        val apiResponse = repository.getTodayLocalForecast(userId, cityId)
        apiResponse.onSuccess {
            databaseRepository.updateHourlyWeatherData(cityId, it)
            return it
        }
        return loadFromDatabase(cityId)
    }

    suspend fun loadFromDatabase(cityId: Int): List<CurrentWeather> {
        val dbResponse = databaseRepository.tryGetHourlyWeather(cityId, 0)
        return dbResponse.getOrThrow()
    }
}