package com.softcat.domain.useCases

import com.softcat.domain.interfaces.DatastoreRepository
import timber.log.Timber
import javax.inject.Inject

class SaveToDatastoreUseCase @Inject constructor(
    private val repository: DatastoreRepository
) {
    suspend operator fun invoke(value: String) {
        Timber.i("${this::class.simpleName} invoked")
        repository.saveCityToDatastore(value)
    }
}