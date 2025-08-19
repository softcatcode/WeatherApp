package com.softcat.weatherapp.presentation.profile

import kotlinx.coroutines.flow.StateFlow

interface ProfileComponent {

    val model: StateFlow<ProfileStore.State>

    fun openSettings()

    fun clearWeatherData()

    fun back()
}