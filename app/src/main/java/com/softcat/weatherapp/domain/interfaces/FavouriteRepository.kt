package com.softcat.weatherapp.domain.interfaces

import com.softcat.weatherapp.domain.entity.City
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface FavouriteRepository {

    val favouriteCities: StateFlow<City>

    fun observeIsFavourite(cityId: Int): Flow<Boolean>

    suspend fun addToFavourite(city: City)

    suspend fun removeFromFavourite(cityId: Int)
}