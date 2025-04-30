package com.softcat.domain.useCases

import android.content.Context
import com.softcat.domain.interfaces.LocationRepository
import javax.inject.Inject

class GetCurrentCityNameUseCase @Inject constructor(
    private val repository: LocationRepository
) {
    operator fun invoke(
        context: Context,
        onCityNameLoaded: (String) -> Unit
    ) = repository.getCurrentCity(context, onCityNameLoaded)
}