package com.softcat.weatherapp.data.implementations

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.softcat.weatherapp.domain.interfaces.DatastoreRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class DatastoreRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
): DatastoreRepository {

    override suspend fun saveCityToDatastore(cityName: String) {
        dataStore.edit { preferences ->
            val datastoreKey = stringPreferencesKey(CITY_NAME_KEY)
            preferences[datastoreKey] = cityName
        }
    }

    override suspend fun getLastCityFromDatastore(): String? {
        val datastoreKey = stringPreferencesKey(CITY_NAME_KEY)
        return dataStore.data.first()[datastoreKey]
    }

    companion object {
        private const val CITY_NAME_KEY = "lastCityName"
    }
}