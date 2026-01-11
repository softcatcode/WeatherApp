package com.softcat.data.implementations

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.gson.Gson
import com.softcat.domain.entity.User
import com.softcat.domain.interfaces.DatastoreRepository
import kotlinx.coroutines.flow.first
import timber.log.Timber
import javax.inject.Inject

class DatastoreRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
): DatastoreRepository {

    override suspend fun saveLastUser(user: User) {
        Timber.i("${this::class.simpleName}.saveLastUser($user)")
        dataStore.edit { preferences ->
            val userKey = stringPreferencesKey(USER_KEY)
            val userJson = Gson().toJson(user)
            preferences[userKey] = userJson
            Timber.i("User $userJson is remembered.")
        }
    }

    override suspend fun clearLastUser() {
        Timber.i("${this::class.simpleName}.clearLastUser()")
        dataStore.edit { preferences ->
            val userKey = stringPreferencesKey(USER_KEY)
            preferences.remove(userKey)
            Timber.i("Last authorized user is forgotten.")
        }
    }

    override suspend fun getLastUser(): User? {
        val datastoreKey = stringPreferencesKey(USER_KEY)
        val userJson = dataStore.data.first()[datastoreKey]
        Timber.i("SUCCESS: $userJson fetched from internal storage.")
        val user = Gson().fromJson(userJson, User::class.java)
        return user
    }

    override suspend fun saveCityToDatastore(cityName: String) {
        Timber.i("${this::class.simpleName}.saveCityToDatastore($cityName)")
        dataStore.edit { preferences ->
            val datastoreKey = stringPreferencesKey(CITY_NAME_KEY)
            preferences[datastoreKey] = cityName
            Timber.i("SUCCESS: ($datastoreKey -> $cityName) saved to internal storage.")
        }
    }

    override suspend fun getLastCityFromDatastore(): String? {
        val datastoreKey = stringPreferencesKey(CITY_NAME_KEY)
        val result = dataStore.data.first()[datastoreKey]
        Timber.i("SUCCESS: $result fetched from internal storage.")
        return result
    }

    companion object {
        private const val CITY_NAME_KEY = "lastCityName"
        private const val USER_KEY = "last_user"
    }
}