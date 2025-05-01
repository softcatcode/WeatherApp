package com.softcat.data.implementations

import com.softcat.domain.interfaces.SearchRepository
import com.softcat.data.mapper.toEntities
import com.softcat.data.network.api.ApiService
import com.softcat.domain.entity.City
import timber.log.Timber
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val apiService: ApiService
): SearchRepository {
    override suspend fun search(query: String): List<City> {
        Timber.i("${this::class.simpleName}.search($query)")
        return apiService.searchCity(query).toEntities()
    }
}