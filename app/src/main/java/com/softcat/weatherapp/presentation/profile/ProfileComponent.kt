package com.softcat.weatherapp.presentation.profile

import android.content.Context
import android.net.Uri
import kotlinx.coroutines.flow.StateFlow

interface ProfileComponent {

    val model: StateFlow<ProfileStore.State>

    fun openSettings()

    fun clearWeatherData()

    fun saveAvatar(context: Context, uri: Uri?, userId: String)

    fun back()

    fun exit()

    fun showDialog()

    fun dismissDialog()
}