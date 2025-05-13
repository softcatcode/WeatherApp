package com.softcat.database.managers.local.region

import com.softcat.database.model.CityDbModel
import com.softcat.database.model.CountryDbModel

interface RegionManager {
    fun getCities(ids: List<Int>): Result<List<CityDbModel>>

    fun getCountries(): Result<List<CountryDbModel>>

    fun saveCity(city: CityDbModel): Result<Unit>

    fun updateCountries(countries: List<CountryDbModel>): Result<List<Int>>

    fun saveCountry(country: CountryDbModel): Result<Int>

    fun deleteCountry(id: Int): Result<Unit>

    fun deleteCity(id: Int): Result<Unit>
}