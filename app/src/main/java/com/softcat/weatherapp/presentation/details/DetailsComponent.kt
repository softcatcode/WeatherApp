package com.softcat.weatherapp.presentation.details

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.softcat.weatherapp.presentation.hourly.HourlyWeatherComponent
import kotlinx.coroutines.flow.StateFlow

interface DetailsComponent {

    val model: StateFlow<DetailsStore.State>

    fun changeFavouriteStatus()

    fun back()

    fun openCityCalendar()

    fun openHourlyWeather(dayIndex: Int)

//    sealed interface Child {
//        data class HourlyWeather(val component: HourlyWeatherComponent): Child
//
//    }
}