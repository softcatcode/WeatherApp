package com.softcat.weatherapp.domain.useCases

import com.softcat.weatherapp.domain.interfaces.CalendarRepository
import javax.inject.Inject

class ResetHighlightedDaysUseCase @Inject constructor(
    private val repository: CalendarRepository
) {
    suspend operator fun invoke() = repository.reset()
}