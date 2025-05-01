package com.softcat.domain.useCases

import com.softcat.domain.entity.Forecast
import com.softcat.domain.interfaces.WeatherRepository
import timber.log.Timber
import javax.inject.Inject

class GetForecastUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(cityId: Int): Forecast {
        Timber.i("${this::class.simpleName} invoked")
        return repository.getForecast(cityId)
    }
}