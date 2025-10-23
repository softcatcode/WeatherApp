package com.softcat.weatherapp.presentation.settings

import kotlinx.coroutines.flow.StateFlow

interface SettingsComponent {

    val model: StateFlow<SettingsStore.State>

    fun sendLogs()

    fun swaggerInterface()

    fun techInterface()

    fun back()
}