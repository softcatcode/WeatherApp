package com.softcat.domain.useCases

import com.softcat.domain.entity.City
import com.softcat.domain.interfaces.SearchRepository
import timber.log.Timber
import javax.inject.Inject

class SearchCityUseCase @Inject constructor(
    private val repository: SearchRepository
) {
    suspend operator fun invoke(query: String): List<City> {
        Timber.i("${this::class.simpleName} invoked")
        return repository.search(query)
    }
}