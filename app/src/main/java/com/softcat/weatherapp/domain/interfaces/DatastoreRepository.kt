package com.softcat.weatherapp.domain.interfaces

interface DatastoreRepository {

    suspend fun saveCityToDatastore(cityName: String)

    suspend fun getLastCityFromDatastore(): String?
}