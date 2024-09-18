package com.softcat.weatherapp.domain.interfaces

import com.softcat.weatherapp.domain.entity.City
import com.softcat.weatherapp.domain.entity.WeatherParameters
import kotlinx.coroutines.flow.Flow

interface CalendarRepository {
    suspend fun selectYearDays(params: WeatherParameters, city: City, year: Int)

    fun getHighlightedDays(): Flow< List<Set<Int>> >
}