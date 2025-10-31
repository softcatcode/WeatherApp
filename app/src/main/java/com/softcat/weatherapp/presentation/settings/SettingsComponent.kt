package com.softcat.weatherapp.presentation.settings

import com.softcat.domain.entity.WebPageType
import kotlinx.coroutines.flow.StateFlow

interface SettingsComponent {

    val model: StateFlow<SettingsStore.State>

    fun sendLogs()

    fun openWebPage(type: WebPageType)

    fun techInterface()

    fun back()
}