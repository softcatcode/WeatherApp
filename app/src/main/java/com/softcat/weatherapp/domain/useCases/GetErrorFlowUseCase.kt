package com.softcat.weatherapp.domain.useCases

import com.softcat.weatherapp.domain.interfaces.CalendarRepository
import javax.inject.Inject

class GetErrorFlowUseCase @Inject constructor(
    private val calendarRep: CalendarRepository
) {
    fun calendarErrors() = calendarRep.getErrorFlow()
}