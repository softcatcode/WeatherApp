package com.softcat.weatherapp.presentation.calendar

import com.arkivanov.mvikotlin.core.store.Store
import com.softcat.weatherapp.domain.entity.City
import com.softcat.weatherapp.domain.entity.WeatherParameters
import com.softcat.weatherapp.domain.entity.WeatherType

interface CalendarStore: Store<CalendarStore.Intent, CalendarStore.State, CalendarStore.Label> {

    data class State(
        val city: City,
        val weatherParams: WeatherParameters,
        val calendarState: CalendarState
    ) {
        sealed interface CalendarState {
            data object Initial: CalendarState

            data object Loading: CalendarState

            data class Error(
                val message: String
            ): CalendarState

            data class Loaded(
                val highlightedDays: List<Set<Int>>
            ): CalendarState
        }
    }

    sealed interface Intent {

        data class ChangeWeatherType(
            val weatherType: WeatherType
        ): Intent

        data class SelectYear(
            val year: Int
        ): Intent

        data class ChangeMinTemp(
            val minTemp: String
        ): Intent

        data class ChangeMaxTemp(
            val maxTemp: String
        ): Intent

        data class ChangeHumidity(
            val humidity: Float
        ): Intent

        data class ChangePrecipitations(
            val humidity: Float
        ): Intent

        data class ChangeWindSpeed(
            val windSpeed: Float
        ): Intent

        data class ChangeSnowVolume(
            val snowVolume: Float
        ): Intent
    }

    sealed interface Label {
        data object BackClicked: Label
    }
}