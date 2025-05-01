package com.softcat.domain.useCases

import com.softcat.domain.interfaces.DatastoreRepository
import timber.log.Timber
import javax.inject.Inject

class GetLastCityFromDatastoreUseCase @Inject constructor(
    private val repository: DatastoreRepository
) {
    suspend operator fun invoke(): String? {
        Timber.i("${this::class.simpleName} invoked")
        return repository.getLastCityFromDatastore()
    }
}