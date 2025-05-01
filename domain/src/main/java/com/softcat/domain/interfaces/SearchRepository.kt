package com.softcat.domain.interfaces

import com.softcat.domain.entity.City

interface SearchRepository {

    suspend fun search(query: String): List<City>
}