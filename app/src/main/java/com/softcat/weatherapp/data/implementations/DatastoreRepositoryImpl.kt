package com.softcat.weatherapp.data.implementations

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.softcat.weatherapp.domain.interfaces.DatastoreRepository
import com.softcat.weatherapp.presentation.extensions.dataStore
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class DatastoreRepositoryImpl @Inject constructor(
    private val context: Context
): DatastoreRepository {

    override suspend fun saveCityToDatastore(cityName: String) {
        context.dataStore.edit { preferences ->
            val datastoreKey = stringPreferencesKey(CITY_NAME_KEY)
            preferences[datastoreKey] = cityName
        }
    }

    override suspend fun getLastCityFromDatastore(): String? {
        val datastoreKey = stringPreferencesKey(CITY_NAME_KEY)
        return context.dataStore.data.first()[datastoreKey]
    }

    companion object {
        private const val CITY_NAME_KEY = "lastCityName"
    }
}