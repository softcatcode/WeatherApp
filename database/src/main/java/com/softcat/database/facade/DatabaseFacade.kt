package com.softcat.database.facade

import com.softcat.database.model.CityDbModel
import com.softcat.database.model.CountryDbModel
import com.softcat.database.model.CurrentWeatherDbModel
import com.softcat.database.model.UserDbModel
import com.softcat.database.model.WeatherDbModel
import com.softcat.database.model.WeatherTypeDbModel

interface DatabaseFacade {

    suspend fun searchCity(query: String): Result<List<CityDbModel>>

    suspend fun createUser(user: UserDbModel): Result<UserDbModel>

    suspend fun verifyUser(name: String, password: String): Result<UserDbModel>

    suspend fun addToFavourites(userId: String, cityId: Int): Result<Unit>

    suspend fun saveCountry(country: CountryDbModel): Result<Int>

    suspend fun saveCity(city: CityDbModel): Result<Unit>

    suspend fun removeFromFavourites(userId: String, cityId: Int): Result<Unit>

    suspend fun getFavouriteCities(userId: String): Result<List<CityDbModel>>

    suspend fun getCountries(): Result<List<CountryDbModel>>

    suspend fun updateCountries(countries: List<CountryDbModel>): Result<List<Int>>

    suspend fun isFavourite(userId: String, cityId: Int): Result<Boolean>

    suspend fun initWeatherTypes(weatherTypes: List<WeatherTypeDbModel>): Result<Unit>

    suspend fun saveWeather(model: WeatherDbModel): Result<Unit>

    suspend fun saveCurrentWeather(model: CurrentWeatherDbModel): Result<Unit>

    suspend fun getCurrentWeather(cityId: Int, dayTimeEpoch: Long): Result<List<CurrentWeatherDbModel>>

    suspend fun getDaysWeather(cityId: Int, startMillis: Long, endMillis: Long): Result<List<WeatherDbModel>>

    suspend fun getWeatherTypes(typeCodes: List<Int>): Result<List<WeatherTypeDbModel>>
}