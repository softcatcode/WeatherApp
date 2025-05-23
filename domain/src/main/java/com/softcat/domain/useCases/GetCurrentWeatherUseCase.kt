package com.softcat.domain.useCases

import com.softcat.domain.entity.CurrentWeather
import com.softcat.domain.entity.Weather
import com.softcat.domain.interfaces.DatabaseLoaderRepository
import com.softcat.domain.interfaces.WeatherRepository
import timber.log.Timber
import javax.inject.Inject

class GetCurrentWeatherUseCase @Inject constructor(
    private val repository: WeatherRepository,
) {
    suspend operator fun invoke(cityId: Int): CurrentWeather {
        Timber.i("${this::class.simpleName} invoked")
        return repository.getWeather(cityId).getOrThrow()
    }
}