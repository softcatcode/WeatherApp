package com.softcat.weatherapp.domain.useCases

import android.content.Context
import com.softcat.weatherapp.domain.interfaces.SearchRepository
import javax.inject.Inject

class GetCurrentCityNameUseCase @Inject constructor(
    private val repository: SearchRepository
) {
    operator fun invoke(
        context: Context,
        latitude: Double,
        longitude: Double,
        onCityObtained: (String) -> Unit
    ) = repository.getCurrentCity(context, latitude, longitude, onCityObtained)
}