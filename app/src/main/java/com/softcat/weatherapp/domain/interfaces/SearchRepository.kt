package com.softcat.weatherapp.domain.interfaces

import android.content.Context
import android.location.Location
import com.softcat.weatherapp.domain.entity.City

interface SearchRepository {

    suspend fun search(query: String): List<City>

    fun getCurrentCity(
        context: Context,
        onCityNameLoaded: (String) -> Unit
    )
}