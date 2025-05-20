package com.softcat.database.managers.local.region

import com.google.firebase.Firebase
import com.google.firebase.database.database
import com.google.gson.Gson
import com.softcat.database.internal.DatabaseRules
import com.softcat.database.model.CityDbModel
import com.softcat.database.model.CountryDbModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

class RegionManagerRemoteImpl @Inject constructor(): RegionManager {

    private val citiesStorage by lazy {
        Firebase.database.getReference(CITIES_STORAGE_NAME)
    }

    private val countriesStorage by lazy {
        Firebase.database.getReference(COUNTRIES_STORAGE_NAME)
    }

    private suspend fun loadCities(): List<CityDbModel>
    {
        return withTimeout(DatabaseRules.TIMEOUT) {
            val result = mutableListOf<CityDbModel>()
            var flag = true
            citiesStorage.get().addOnSuccessListener {
                val iterator = it.children.iterator()
                while (iterator.hasNext()) {
                    val cityJson = iterator.next().value.toString()
                    val city = Gson().fromJson(cityJson, CityDbModel::class.java)
                    result.add(city)
                }
                flag = false
            }.addOnFailureListener {
                throw it
            }
            while (flag) {
                delay(1L)
            }
            result
        }
    }

    private suspend fun loadCountries(): List<CountryDbModel>
    {
        return withTimeout(DatabaseRules.TIMEOUT) {
            val result = mutableListOf<CountryDbModel>()
            var flag = true
            countriesStorage.get().addOnSuccessListener {
                val iterator = it.children.iterator()
                while (iterator.hasNext()) {
                    val countryJson = iterator.next().value.toString()
                    val country = Gson().fromJson(countryJson, CountryDbModel::class.java)
                    result.add(country)
                }
                flag = false
            }.addOnFailureListener {
                throw it
            }
            while (flag) {
                delay(1L)
            }
            result
        }
    }

    override suspend fun getCities(ids: List<Int>): Result<List<CityDbModel>> {
        return try {
            val cities = loadCities()
            val result = ids.mapNotNull { cityId ->
                cities.find { it.id == cityId }
            }
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getCountries(): Result<List<CountryDbModel>> {
        return try {
            val countries = loadCountries()
            Result.success(countries)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun saveCityList(cities: List<CityDbModel>) {
        withTimeout(DatabaseRules.TIMEOUT) {
            var flag = true
            citiesStorage.setValue(cities).addOnSuccessListener {
                flag = false
            }.addOnFailureListener {
                throw it
            }
            while (flag) {
                delay(1L)
            }
        }
    }

    private suspend fun saveCountryList(countries: List<CountryDbModel>) {
        withTimeout(DatabaseRules.TIMEOUT) {
            var flag = true
            countriesStorage.setValue(countries).addOnSuccessListener {
                flag = false
            }.addOnFailureListener {
                throw it
            }
            while (flag) {
                delay(1L)
            }
        }
    }

    override suspend fun saveCity(city: CityDbModel): Result<Unit> {
        val cities = try {
            loadCities().map {
                it.copy( name = "\"${it.name}\"")
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
        val name = if (city.name[0] == '"') city.name else "\"${city.name}\""
        val newCities = cities.toMutableList().apply {
            add(city.copy(name = name))
        }
        return try {
            saveCityList(newCities)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateCountries(countries: List<CountryDbModel>): Result<List<Int>> {
        val existingCountries = try {
            loadCountries().map {
                it.copy(name = "\"${it.name}\"")
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }

        // Fix countries fields.
        val maxId = if (countries.isEmpty()) 0 else countries.maxOf { it.id }
        val idList = List(countries.size) {
            if (countries[it].id == 0)
                it + maxId + 1
            else
                countries[it].id
        }
        val nameList = List(countries.size) {
            val name = countries[it].name
            if (name[0] == '"')
                name
            else
                "\"$name\""
        }

        val newCountries = existingCountries.toMutableList()
        countries.mapIndexed { index, elem ->
            elem.copy(id = idList[index], name = nameList[index])
        }.map { country ->
            val duplicate = existingCountries.find { it.name == country.name }
            duplicate?.let { return@map it.id }
            newCountries.add(country)
            country.id
        }
        return try {
            saveCountryList(newCountries)
            Result.success(idList)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun saveCountry(country: CountryDbModel): Result<Int> {
        val countries = try {
            loadCountries().map {
                it.copy(name = "\"${it.name}\"")
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
        val duplicate = countries.find { it.name == country.name }
        if (duplicate != null)
            return Result.success(duplicate.id)
        val newCountryId = if (countries.isEmpty()) 1 else { countries.maxOf { it.id } + 1 }
        val newCountryName = if (country.name[0] == '"') country.name else "\"${country.name}\""
        val newCountries = countries.toMutableList().apply {
            add(country.copy(id = newCountryId, name = newCountryName))
        }
        return try {
            saveCountryList(newCountries)
            Result.success(newCountryId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteCountry(id: Int): Result<Unit> {
        val countries = try {
            loadCountries()
        } catch (e: Exception) {
            return Result.failure(e)
        }
        val newCountries = countries.toMutableList().apply {
            removeIf { it.id == id }
        }
        return try {
            saveCountryList(newCountries)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteCity(id: Int): Result<Unit> {
        val cities = try {
            loadCities()
        } catch (e: Exception) {
            return Result.failure(e)
        }
        val newCities = cities.toMutableList().apply {
            removeIf { it.id == id }
        }
        return try {
            saveCityList(newCities)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    companion object {
        private const val CITIES_STORAGE_NAME = "Cities"
        private const val COUNTRIES_STORAGE_NAME = "Countries"
    }
}