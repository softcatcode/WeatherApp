package com.softcat.data.implementations

import com.softcat.data.mapper.toDbModel
import com.softcat.domain.interfaces.SearchRepository
import com.softcat.data.mapper.toEntities
import com.softcat.data.network.api.ApiService
import com.softcat.database.facade.DatabaseFacade
import com.softcat.database.model.CountryDbModel
import com.softcat.domain.entity.City
import timber.log.Timber
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val database: DatabaseFacade
): SearchRepository {

    override suspend fun search(query: String): List<City> {
        Timber.i("${this::class.simpleName}.search($query)")
        val cities = apiService.searchCity(query).toEntities()

        database.updateCountries(
            countries = cities.map { CountryDbModel(0, it.country) }
        ).onSuccess { countryIds ->
            cities.mapIndexed { index, city ->
                city.toDbModel(countryIds[index])
            }.forEach {
                database.saveCity(it)
            }
        }
        return cities
    }
}