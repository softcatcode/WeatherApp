package com.softcat.weatherapp.data.implementations

import com.softcat.weatherapp.data.mapper.toEntities
import com.softcat.weatherapp.data.network.api.ApiService
import com.softcat.weatherapp.domain.interfaces.SearchRepository
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val apiService: ApiService
): SearchRepository {
    override suspend fun search(query: String) = apiService.searchCity(query).toEntities()
}