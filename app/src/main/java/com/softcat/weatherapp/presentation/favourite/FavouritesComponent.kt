package com.softcat.weatherapp.presentation.favourite

import com.softcat.weatherapp.domain.entity.City
import kotlinx.coroutines.flow.StateFlow

interface FavouritesComponent {
    val model: StateFlow<FavouritesStore.State>

    fun onSearchClick()

    fun onAddToFavouritesClick()

    fun onCityItemClick(city: City)
}