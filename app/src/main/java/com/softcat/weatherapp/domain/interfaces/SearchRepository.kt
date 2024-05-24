package com.softcat.weatherapp.domain.interfaces

import com.softcat.weatherapp.domain.entity.City

interface SearchRepository {

    suspend fun search(query: String): List<City>
}