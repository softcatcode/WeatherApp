package com.softcat.domain.interfaces

import com.softcat.domain.entity.City
import kotlinx.coroutines.flow.Flow

interface FavouriteRepository {

    fun getFavouriteCities(): Flow<List<City>>

    fun observeIsFavourite(cityId: Int): Flow<Boolean>

    suspend fun addToFavourite(city: City)

    suspend fun removeFromFavourite(cityId: Int)
}