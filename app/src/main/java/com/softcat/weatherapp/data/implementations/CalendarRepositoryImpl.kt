package com.softcat.weatherapp.data.implementations

import com.softcat.weatherapp.domain.entity.City
import com.softcat.weatherapp.domain.entity.WeatherParameters
import com.softcat.weatherapp.domain.interfaces.CalendarRepository
import kotlinx.coroutines.flow.Flow

class CalendarRepositoryImpl: CalendarRepository {
    override suspend fun selectYearDays(params: WeatherParameters, city: City, year: Int) {
        TODO("Not yet implemented")
    }

    override fun getHighlightedDays(): Flow<List<Set<Int>>> {
        TODO("Not yet implemented")
    }
}