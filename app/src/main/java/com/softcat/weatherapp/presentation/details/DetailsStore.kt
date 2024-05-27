package com.softcat.weatherapp.presentation.details

import com.arkivanov.mvikotlin.core.store.Store
import com.softcat.weatherapp.domain.entity.City
import com.softcat.weatherapp.domain.entity.Forecast

interface DetailsStore: Store<DetailsStore.Intent, DetailsStore.State, DetailsStore.Label> {

    sealed interface Intent {
        data object BackClicked: Intent
        data object ChangeFavouriteStatusClicked: Intent
    }

    data class State(
        val city: City,
        val forecastState: ForecastState,
        val isFavourite: Boolean
    ) {
        sealed interface ForecastState {

            data object Initial: ForecastState

            data object Loading: ForecastState

            data object Error: ForecastState

            data class Loaded(val forecast: Forecast): ForecastState
        }
    }

    sealed interface Label {
        data object BackClicked: Label
    }
}