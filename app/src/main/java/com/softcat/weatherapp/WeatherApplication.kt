package com.softcat.weatherapp

import android.app.Application
import androidx.datastore.preferences.preferencesDataStore
import com.softcat.weatherapp.di.components.DaggerApplicationComponent

class WeatherApplication: Application() {

    val dataStore by preferencesDataStore(
        name = DATASTORE_NAME
    )

    val component by lazy {
        DaggerApplicationComponent.factory().create(this, dataStore)
    }

    companion object {
        private const val DATASTORE_NAME = "weather_datastore"
    }
}