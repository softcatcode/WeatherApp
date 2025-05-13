package com.softcat.database.facade

import com.softcat.database.model.CityDbModel
import com.softcat.database.model.CountryDbModel
import com.softcat.database.model.UserDbModel

interface DatabaseFacade {

    suspend fun createUser(login: String, email: String, password: String): Result<UserDbModel>

    suspend fun verifyUser(name: String, password: String): Result<UserDbModel>

    suspend fun addToFavourites(userId: Int, cityId: Int): Result<Unit>

    suspend fun saveCountry(country: CountryDbModel): Result<Int>

    suspend fun saveCity(city: CityDbModel): Result<Unit>

    suspend fun removeFromFavourites(userId: Int, cityId: Int): Result<Unit>

    suspend fun getFavouriteCities(userId: Int): Result<List<CityDbModel>>

    suspend fun getCountries(): Result<List<CountryDbModel>>

    suspend fun updateCountries(countries: List<CountryDbModel>): Result<List<Int>>

    suspend fun isFavourite(userId: Int, cityId: Int): Result<Boolean>
}