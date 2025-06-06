package com.softcat.database.managers.local.region

import com.softcat.database.internal.CursorMapperInterface
import com.softcat.database.internal.sqlExecutor.SQLiteExecutor
import com.softcat.database.model.CityDbModel
import com.softcat.database.model.CountryDbModel
import javax.inject.Inject

class RegionManagerImpl @Inject constructor(
    private val executor: SQLiteExecutor,
    private val mapper: CursorMapperInterface
): RegionManager {

    private fun match(query: String, name: String): Boolean {
        val q = query.lowercase()
        val s = name.lowercase()
        val lastInd = s.length - q.length
        for (i in 0..lastInd) {
            if (s.substring(i, i + q.length) == q)
                return true
        }
        return false
    }

    override suspend fun searchCity(query: String): Result<List<CityDbModel>> {
        if (query.isBlank())
            return Result.success(emptyList())
        val cities = try {
            val cursor = executor.getCities()
            mapper.toCityModels(cursor)
        } catch (e: Exception) {
            return Result.failure(e)
        }
        val result = cities.filter { match(query, it.name) }
        return Result.success(result)
    }

    override suspend fun getCities(ids: List<Int>): Result<List<CityDbModel>> {
        return try {
            val result = ids.mapNotNull { cityId ->
                val cursor = executor.getCity(cityId)
                mapper.toCityModels(cursor).firstOrNull()
            }
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getCountries(): Result<List<CountryDbModel>> {
        return try {
            val result = mapper.toCountryModels(executor.getCountries())
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun saveCity(city: CityDbModel): Result<Unit> {
        return try {
            Result.success(executor.insertCity(city))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateCountries(countries: List<CountryDbModel>): Result<List<Int>> {
        return try {
            val result = countries.map { country ->
                saveCountry(country).getOrThrow()
            }
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    @Synchronized
    override fun saveCountry(country: CountryDbModel): Result<Int> {
        try {
            val duplicateCountryId = try {
                mapper.toInt(executor.getCountryId(country.name))
            } catch (_: Exception) {
                null
            }
            duplicateCountryId?.let {
                return Result.success(it)
            }
            val id = mapper.toInt(executor.insertCountry(country))
            return Result.success(id)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    override suspend fun deleteCountry(id: Int): Result<Unit> {
        return try {
            Result.success(executor.deleteCountry(id))
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    override suspend fun deleteCity(id: Int): Result<Unit> {
        return try {
            Result.success(executor.deleteCity(id))
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}