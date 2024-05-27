package com.softcat.weatherapp.presentation.search

import com.softcat.weatherapp.domain.entity.City
import kotlinx.coroutines.flow.StateFlow

interface SearchComponent {
    val model: StateFlow<SearchStore.State>

    fun changeSearchQuery(query: String)

    fun back()

    fun clickSearch()

    fun clickCity(city: City)
}