package com.softcat.weatherapp.domain.useCases

import android.content.Context
import com.softcat.weatherapp.domain.interfaces.LocationRepository
import javax.inject.Inject

class GetCurrentCityNameUseCase @Inject constructor(
    private val repository: LocationRepository
) {
    operator fun invoke(
        context: Context,
        onCityNameLoaded: (String) -> Unit
    ) = repository.getCurrentCity(context, onCityNameLoaded)
}