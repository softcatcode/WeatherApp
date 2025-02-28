package com.softcat.weatherapp.domain.interfaces

import android.content.Context
import com.softcat.weatherapp.domain.entity.City

interface SearchRepository {

    suspend fun search(query: String): List<City>

    fun getCurrentCity(
        context: Context,
        latitude: Double,
        longitude: Double,
        onCityObtained: (String) -> Unit
    )
}