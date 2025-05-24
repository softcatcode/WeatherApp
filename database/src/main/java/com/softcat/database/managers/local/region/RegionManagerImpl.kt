package com.softcat.database.managers.local.region

import com.softcat.database.internal.sqlExecutor.SQLiteExecutor
import com.softcat.database.mapper.toCityModels
import com.softcat.database.mapper.toCountriesModels
import com.softcat.database.mapper.toInt
import com.softcat.database.model.CityDbModel
import com.softcat.database.model.CountryDbModel
import javax.inject.Inject

class RegionManagerImpl @Inject constructor(
    private val executor: SQLiteExecutor
): RegionManager {

    override suspend fun getCities(ids: List<Int>): Result<List<CityDbModel>> {
        return try {
            val result = ids.mapNotNull { cityId ->
                val cursor = executor.getCity(cityId)
                toCityModels(cursor).firstOrNull()
            }
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getCountries(): Result<List<CountryDbModel>> {
        return try {
            val result = toCountriesModels(executor.getCountries())
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
        return try {
            val countries = toCountriesModels(executor.getCountries())
            if (countries.find { it.name == country.name } == null) {
                val id = toInt(executor.insertCountry(country))
                Result.success(id)
            } else {
                val id = toInt(executor.getCountryId(country.name))
                Result.success(id)
            }
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