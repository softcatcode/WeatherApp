package com.softcat.domain.useCases

import com.softcat.domain.interfaces.DatabaseLoaderRepository
import timber.log.Timber
import javax.inject.Inject

class LoadWeatherTypesUseCase @Inject constructor(
    private val repository: DatabaseLoaderRepository
) {
    suspend operator fun invoke() {
        Timber.i("${this::class.simpleName} invoked")
        repository.loadWeatherTypesToDatabase()
    }
}