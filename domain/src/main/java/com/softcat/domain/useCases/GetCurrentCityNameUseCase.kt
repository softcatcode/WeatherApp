package com.softcat.domain.useCases

import android.content.Context
import com.softcat.domain.interfaces.LocationRepository
import timber.log.Timber
import javax.inject.Inject

class GetCurrentCityNameUseCase @Inject constructor(
    private val repository: LocationRepository
) {
    operator fun invoke(
        context: Context,
        onCityNameLoaded: (String) -> Unit
    ) {
        Timber.i("${this::class.simpleName} invoked")
        repository.getCurrentCity(context, onCityNameLoaded)
    }
}