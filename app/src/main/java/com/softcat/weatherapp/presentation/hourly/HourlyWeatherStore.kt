package com.softcat.weatherapp.presentation.hourly

import com.arkivanov.mvikotlin.core.store.Store
import com.softcat.domain.entity.CurrentWeather

interface HourlyWeatherStore:
    Store<HourlyWeatherStore.Intent, HourlyWeatherStore.State, HourlyWeatherStore.Label> {
    sealed interface Intent {
        data object BackClick: Intent
    }

    sealed interface State {
        data object Initial: State

        data class Forecast(val hoursWeather: List<CurrentWeather>): State
    }

    sealed interface Label {
        data object BackClick: Label
    }
}