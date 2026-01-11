package com.softcat.domain.interfaces

import com.softcat.domain.entity.User

interface DatastoreRepository {

    suspend fun saveLastUser(user: User)

    suspend fun clearLastUser()

    suspend fun getLastUser(): User?

    suspend fun saveCityToDatastore(cityName: String)

    suspend fun getLastCityFromDatastore(): String?
}