package com.softcat.weatherapp.domain.useCases

import com.softcat.weatherapp.domain.interfaces.WeatherRepository
import javax.inject.Inject

class GetForecastUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(cityId: Int) = repository.getForecast(cityId)
}