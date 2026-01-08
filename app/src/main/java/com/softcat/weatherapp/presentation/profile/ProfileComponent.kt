package com.softcat.weatherapp.presentation.profile

import android.net.Uri
import kotlinx.coroutines.flow.StateFlow

interface ProfileComponent {

    val model: StateFlow<ProfileStore.State>

    fun openSettings()

    fun clearWeatherData()

    fun saveAvatar(uri: Uri?)

    fun back()
}