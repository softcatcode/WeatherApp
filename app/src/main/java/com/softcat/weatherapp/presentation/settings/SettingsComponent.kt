package com.softcat.weatherapp.presentation.settings

import kotlinx.coroutines.flow.StateFlow

interface SettingsComponent {

    val model: StateFlow<SettingsStore.State>

    fun back()

    fun switchLab9Setting()
}