package com.softcat.weatherapp.domain.useCases

import com.softcat.weatherapp.domain.interfaces.CalendarRepository
import javax.inject.Inject

class GetErrorFlowUseCase @Inject constructor(
    private val calendarRep: CalendarRepository
) {
    operator fun invoke() = calendarRep.getErrorFlow()
}