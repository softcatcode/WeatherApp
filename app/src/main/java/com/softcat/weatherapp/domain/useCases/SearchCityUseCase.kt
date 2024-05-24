package com.softcat.weatherapp.domain.useCases

import com.softcat.weatherapp.domain.interfaces.SearchRepository
import javax.inject.Inject

class SearchCityUseCase @Inject constructor(
    private val repository: SearchRepository
) {
    suspend operator fun invoke(query: String) = repository.search(query)
}