package com.softcat.domain.useCases

import com.softcat.domain.interfaces.WeatherRepository
import javax.inject.Inject

class GetTodayForecastUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(cityId: Int) = repository.getTodayLocalForecast(cityId)
}