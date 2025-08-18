package com.softcat.weatherapp.presentation.favourite

import com.softcat.domain.entity.City
import kotlinx.coroutines.flow.StateFlow

interface FavouritesComponent {
    val model: StateFlow<FavouritesStore.State>

    fun onSearchClick()

    fun onProfileClick()

    fun onAddToFavouritesClick()

    fun onCityItemClick(city: City)

    fun reloadCities()
}