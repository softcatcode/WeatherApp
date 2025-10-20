package com.softcat.weatherapp

import android.app.Application
import androidx.datastore.preferences.preferencesDataStore
import com.google.firebase.FirebaseApp
import com.softcat.weatherapp.di.components.DaggerApplicationComponent
import java.io.File

class WeatherApplication: Application() {

    val dataStore by preferencesDataStore(
        name = DATASTORE_NAME
    )

    val component by lazy {
        DaggerApplicationComponent.factory().create(this, dataStore)
    }

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        INTERNAL_STORAGE = filesDir
    }

    companion object {
        private const val DATASTORE_NAME = "weather_datastore"
        lateinit var INTERNAL_STORAGE: File
            private set
    }
}