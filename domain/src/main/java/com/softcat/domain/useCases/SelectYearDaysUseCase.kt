package com.softcat.domain.useCases

import com.softcat.domain.entity.City
import com.softcat.domain.entity.WeatherParameters
import com.softcat.domain.interfaces.CalendarRepository
import timber.log.Timber
import javax.inject.Inject

class SelectYearDaysUseCase @Inject constructor(
    private val repository: CalendarRepository
) {
    suspend operator fun invoke(
        params: WeatherParameters,
        city: City,
        year: Int
    ): Result<List<Set<Int>>> {
        Timber.i("${this::class.simpleName} invoked")
        return repository.selectYearDays(params, city, year)
    }
}