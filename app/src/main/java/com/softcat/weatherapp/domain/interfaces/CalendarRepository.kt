package com.softcat.weatherapp.domain.interfaces

import com.softcat.weatherapp.domain.entity.City
import com.softcat.weatherapp.domain.entity.WeatherParameters
import kotlinx.coroutines.flow.SharedFlow

interface CalendarRepository {
    suspend fun selectYearDays(params: WeatherParameters, city: City, selectedYear: Int)

    fun getHighlightedDays(): SharedFlow<List<Set<Int>>>

    suspend fun reset()

    fun getErrorFlow(): SharedFlow<Throwable>
}