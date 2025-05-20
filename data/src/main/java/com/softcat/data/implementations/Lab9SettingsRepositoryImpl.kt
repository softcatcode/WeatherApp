package com.softcat.data.implementations

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.softcat.domain.interfaces.Lab9SettingsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch
import javax.inject.Inject

class Lab9SettingsRepositoryImpl @Inject constructor(
    private val store: DataStore<Preferences>
): Lab9SettingsRepository {

    override suspend fun init() {
        val key = stringPreferencesKey(KEY)
        val values = store.data.first()
        if (!values.contains(key)) {
            store.edit {
                it[key] = LOCAL
            }
        }
    }

    override suspend fun switchRegionStorage() {
        val key = stringPreferencesKey(KEY)
        val currentValue = store.data.first()[key]
        val nextValue = if (currentValue == LOCAL) REMOTE else LOCAL
        store.edit { it[key] = nextValue }
    }

    override suspend fun isLocalRegionStorage(): Flow<Boolean> {
        val key = stringPreferencesKey(KEY)
        return store.data.transform {
            try {
                emit(it[key] == LOCAL)
            } catch (e: Exception) {
                Log.i("mumu", e.message.toString())
            }
        }
    }

    override suspend fun isLocal(): Boolean {
        val key = stringPreferencesKey(KEY)
        return store.data.first()[key] == LOCAL
    }

    companion object {
        private const val KEY = "storing_strategy"
        private const val LOCAL = "local"
        private const val REMOTE = "remote"
    }
}