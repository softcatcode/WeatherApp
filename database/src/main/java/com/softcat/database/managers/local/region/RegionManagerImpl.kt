package com.softcat.database.managers.local.region

import com.softcat.database.exceptions.DuplicateCountryException
import com.softcat.database.internal.sqlExecutor.SQLiteExecutor
import com.softcat.database.internal.toCitiesModels
import com.softcat.database.internal.toCountriesModels
import com.softcat.database.internal.toInt
import com.softcat.database.model.CityDbModel
import com.softcat.database.model.CountryDbModel
import javax.inject.Inject

class RegionManagerImpl @Inject constructor(
    private val executor: SQLiteExecutor
): RegionManager {

    override fun getCities(ids: List<Int>): Result<List<CityDbModel>> {
        return try {
            val result = ids.mapNotNull { cityId ->
                val cursor = executor.getCity(cityId)
                toCitiesModels(cursor).firstOrNull()
            }
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getCountries(): Result<List<CountryDbModel>> {
        return try {
            val result = toCountriesModels(executor.getCountries())
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun saveCity(city: CityDbModel): Result<Unit> {
        return try {
            Result.success(executor.insertCity(city))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun updateCountries(countries: List<CountryDbModel>): Result<List<Int>> {
        return try {
            val models = toCountriesModels(executor.getCountries())
            val result = countries.map { country ->
                val model = models.find { it.name == country.name }
                model?.id ?: toInt(executor.insertCountry(country))
            }
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun saveCountry(country: CountryDbModel): Result<Int> {
        return try {
            val countries = toCountriesModels(executor.getCountries())
            if (countries.find { it.name == country.name } == null) {
                val id = toInt(executor.insertCountry(country))
                Result.success(id)
            } else {
                Result.failure(DuplicateCountryException(country.name))
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    override fun deleteCountry(id: Int): Result<Unit> {
        return try {
            Result.success(executor.deleteCountry(id))
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    override fun deleteCity(id: Int): Result<Unit> {
        return try {
            Result.success(executor.deleteCity(id))
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}