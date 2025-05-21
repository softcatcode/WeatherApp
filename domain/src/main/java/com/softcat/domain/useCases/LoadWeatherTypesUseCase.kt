package com.softcat.domain.useCases

import com.softcat.domain.entity.City
import com.softcat.domain.interfaces.WeatherRepository
import timber.log.Timber
import javax.inject.Inject

class LoadWeatherTypesUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke() {
        Timber.i("${this::class.simpleName} invoked")
        repository.loadWeatherTypesToDatabase()
    }
}