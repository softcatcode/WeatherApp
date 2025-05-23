package com.softcat.database.managers.local.region

import com.softcat.database.model.CityDbModel
import com.softcat.database.model.CountryDbModel

interface RegionManager {
    suspend fun getCities(ids: List<Int>): Result<List<CityDbModel>>

    suspend fun getCountries(): Result<List<CountryDbModel>>

    suspend fun saveCity(city: CityDbModel): Result<Unit>

    suspend fun updateCountries(countries: List<CountryDbModel>): Result<List<Int>>

    suspend fun saveCountry(country: CountryDbModel): Result<Int>

    suspend fun deleteCountry(id: Int): Result<Unit>

    suspend fun deleteCity(id: Int): Result<Unit>
}