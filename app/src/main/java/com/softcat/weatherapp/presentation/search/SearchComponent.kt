package com.softcat.weatherapp.presentation.search

import com.softcat.domain.entity.City
import kotlinx.coroutines.flow.StateFlow

interface SearchComponent {
    val model: StateFlow<SearchStore.State>

    fun changeSearchQuery(query: String)

    fun back()

    fun clickCity(city: City)
}