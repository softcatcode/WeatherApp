package com.softcat.weatherapp.presentation.calendar

import com.arkivanov.mvikotlin.core.store.Store
import com.softcat.domain.entity.City
import com.softcat.domain.entity.WeatherParameters
import com.softcat.domain.entity.WeatherType

interface CalendarStore: Store<CalendarStore.Intent, CalendarStore.State, CalendarStore.Label> {

    data class State(
        val userId: String,
        val city: City,
        val year: Int,
        val weatherParams: WeatherParameters,
        val calendarState: CalendarState
    ) {
        sealed interface CalendarState {
            data object Initial: CalendarState

            data object Loading: CalendarState

            data class Error(
                val throwable: Throwable
            ): CalendarState

            data class Loaded(
                val highlightedDays: List<Set<Int>>
            ): CalendarState
        }
    }

    sealed interface Intent {

        data object BackClicked: Intent

        data object LoadHighlightedDays: Intent

        data class ChangeWeatherType(
            val weatherType: WeatherType
        ): Intent

        data class SelectYear(
            val city: City,
            val year: Int
        ): Intent

        data class ChangeMinTemp(
            val minTemp: String
        ): Intent

        data class ChangeMaxTemp(
            val maxTemp: String
        ): Intent

        data class ChangeHumidity(
            val humidity: IntRange
        ): Intent

        data class ChangePrecipitations(
            val precipitations: IntRange
        ): Intent

        data class ChangeWindSpeed(
            val windSpeed: IntRange
        ): Intent

        data class ChangeSnowVolume(
            val snowVolume: IntRange
        ): Intent
    }

    sealed interface Label {
        data object BackClicked: Label
    }
}