package com.softcat.weatherapp.presentation.web

import kotlinx.coroutines.flow.StateFlow

interface WebComponent {
    fun back()

    val model: StateFlow<WebStore.State>
}