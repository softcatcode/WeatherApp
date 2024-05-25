package com.softcat.weatherapp

import android.app.Application
import com.softcat.weatherapp.di.components.DaggerApplicationComponent

class WeatherApplication: Application() {
    val component by lazy {
        DaggerApplicationComponent.factory().create(this)
    }
}