package com.softcat.domain.useCases

import com.softcat.domain.entity.City
import com.softcat.domain.interfaces.DatabaseLoaderRepository
import com.softcat.domain.interfaces.SearchRepository
import timber.log.Timber
import javax.inject.Inject

class SearchCityUseCase @Inject constructor(
    private val repository: SearchRepository,
    private val databaseRepository: DatabaseLoaderRepository
) {

    private suspend fun searchLocally(query: String): Result<List<City>> {
        Timber.i("${this::class.simpleName}.searchLocally($query) invoked")
        try {
            val result = databaseRepository.searchCities(query).getOrThrow()
            return Result.success(result)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    suspend operator fun invoke(userId: String, query: String): Result<List<City>> {
        Timber.i("${this::class.simpleName} invoked")
        return try {
            val result = repository.search(userId, query)
            Result.success(result)
        } catch (_: Exception) {
            searchLocally(query)
        }
    }
}