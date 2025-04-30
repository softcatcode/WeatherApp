package com.softcat.domain.useCases

import com.softcat.domain.interfaces.DatastoreRepository
import javax.inject.Inject

class GetLastCityFromDatastoreUseCase @Inject constructor(
    private val repository: DatastoreRepository
) {
    suspend operator fun invoke() = repository.getLastCityFromDatastore()
}