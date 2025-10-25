package com.softcat.weatherapp.presentation.tech_interface

import com.arkivanov.mvikotlin.core.store.Store
import com.softcat.domain.entity.City

interface TechIntComponentStore:
    Store<TechIntComponentStore.Intent, TechIntComponentStore.State, TechIntComponentStore.Label> {
    sealed interface Intent {
        data class GetFavouriteCities(
            val userId: String
        ): Intent

        data class SearchUseCase(
            val query: String
        ): Intent

        data class GetCurrentWeather(
            val cityName: String
        ): Intent
        data class GetForecast(
            val cityName: String
        ): Intent

        data class AddToFavourites(
            val userId: String,
            val cityName: String
        ): Intent

        data class RemoveFromFavourites(
            val userId: String,
            val cityId: Int
        ): Intent

        data class SelectUseCase(
            val index: Int
        ): Intent

        data object BackClick: Intent
    }

    data class State(
        val selectedUseCaseIndex: Int,
        val answer: String
    )

    sealed interface Label {
        data object BackClicked: Label
    }
}