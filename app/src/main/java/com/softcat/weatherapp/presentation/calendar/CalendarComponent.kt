package com.softcat.weatherapp.presentation.calendar

import com.softcat.weatherapp.domain.entity.WeatherType
import kotlinx.coroutines.flow.StateFlow

interface CalendarComponent {

    val model: StateFlow<CalendarStore.State>

    fun back()

    fun highlightDays()

    // scroll view selectors
    fun selectWeatherType(type: WeatherType)
    fun selectYear(year: Int)

    // text field parameters
    fun changeMinTemp(temp: String)
    fun changeMaxTemp(temp: String)

    // slider parameters
    fun changeWindSpeed(newValue: ClosedFloatingPointRange<Float>)
    fun changeHumidity(newValue: ClosedFloatingPointRange<Float>)
    fun changePrecipitations(newValue: ClosedFloatingPointRange<Float>)
    fun changeSnowVolume(newValue: ClosedFloatingPointRange<Float>)
}