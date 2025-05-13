package com.softcat.weatherapp.presentation.search

import com.arkivanov.mvikotlin.core.store.Store
import com.softcat.domain.entity.City
import com.softcat.domain.entity.User

interface SearchStore: Store<SearchStore.Intent, SearchStore.State, SearchStore.Label> {

    sealed interface Intent {
        data class ChangeSearchQuery(val query: String): Intent

        data object BackClick: Intent

        data class CityClick(val city: City): Intent
    }

    sealed interface Label {
        data object BackClick : Label

        data object SavedToFavourites : Label

        data class OpenForecast(val city: City) : Label
    }

    data class State(
        val user: User,
        val searchQuery: String,
        val searchState: SearchState
    ) {
        sealed interface SearchState {
            data object Initial: SearchState

            data object Loading: SearchState

            data object Error: SearchState

            data object EmptyResult: SearchState

            data class Success(val cities: List<City>): SearchState
        }

    }
}