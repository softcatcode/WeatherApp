package com.softcat.weatherapp.domain.useCases

import com.softcat.weatherapp.domain.entity.City
import com.softcat.weatherapp.domain.entity.WeatherParameters
import com.softcat.weatherapp.domain.interfaces.CalendarRepository
import javax.inject.Inject

class SelectYearDaysUseCase @Inject constructor(
    private val repository: CalendarRepository
) {
    suspend operator fun invoke(params: WeatherParameters, city: City, year: Int) =
        repository.selectYearDays(params, city, year)
}