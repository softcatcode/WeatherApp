package com.softcat.weatherapp.presentation.swagger

import kotlinx.coroutines.flow.StateFlow

interface SwaggerComponent {
    fun back()

    val model: StateFlow<SwaggerStore.State>
}