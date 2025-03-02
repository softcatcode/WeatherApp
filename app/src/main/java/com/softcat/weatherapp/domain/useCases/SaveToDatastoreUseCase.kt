package com.softcat.weatherapp.domain.useCases

import com.softcat.weatherapp.domain.interfaces.DatastoreRepository
import javax.inject.Inject

class SaveToDatastoreUseCase @Inject constructor(
    private val repository: DatastoreRepository
) {
    suspend operator fun invoke(value: String) =
        repository.saveCityToDatastore(value)
}