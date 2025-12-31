package com.softcat.weatherapp.presentation.tech_interface

import kotlinx.coroutines.flow.StateFlow

interface TechIntComponent {

    val model: StateFlow<TechIntComponentStore.State>

    fun search(query: String)

    fun getCurrentWeather(cityName: String)

    fun getForecast(cityName: String)

    fun getFavouriteCities()

    fun addToFavourites(cityName: String)

    fun removeFromFavourites(cityId: Int)

    fun logIn(name: String, password: String)

    fun register(name: String, email: String, password: String)

    fun selectUseCase(index: Int)
}