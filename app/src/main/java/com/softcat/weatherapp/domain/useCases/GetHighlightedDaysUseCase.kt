package com.softcat.weatherapp.domain.useCases

import com.softcat.weatherapp.domain.interfaces.CalendarRepository
import javax.inject.Inject

class GetHighlightedDaysUseCase @Inject constructor(
    private val repository: CalendarRepository
) {
    operator fun invoke() = repository.getHighlightedDays()
}