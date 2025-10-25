package com.softcat.domain.interfaces

import com.softcat.domain.entity.City
import com.softcat.domain.entity.WeatherParameters

interface CalendarRepository {

    suspend fun selectYearDays(
        userId: String,
        params: WeatherParameters,
        city: City,
        selectedYear: Int
    ): Result<List<Set<Int>>>
}