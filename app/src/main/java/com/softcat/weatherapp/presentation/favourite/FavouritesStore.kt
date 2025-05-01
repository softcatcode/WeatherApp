package com.softcat.weatherapp.presentation.favourite

import com.arkivanov.mvikotlin.core.store.Store
import com.softcat.domain.entity.City

interface FavouritesStore: Store<FavouritesStore.Intent, FavouritesStore.State, FavouritesStore.Label> {

    sealed interface Intent {

        data class CityItemClicked(val city: City): Intent

        data object AddFavouritesClicked: Intent

        data object SearchClicked: Intent

        data class ReloadCities(
            val cities: List<City>
        ): Intent
    }

    data class State(
        val cityItems: List<CityItem>
    ) {

        data class CityItem(
            val city: City,
            val weatherState: WeatherState
        )

        sealed interface WeatherState {

            data object Initial: WeatherState

            data object Loading: WeatherState

            data object Error: WeatherState

            data class Content(
                val tempC: Float,
                val iconUrl: String
            ): WeatherState
        }
    }

    sealed interface Label {
        data class CityItemClicked(val city: City): Label

        data object AddFavouritesClicked: Label

        data object SearchClicked: Label
    }
}