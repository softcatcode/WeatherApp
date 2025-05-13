package com.softcat.domain.interfaces

import com.softcat.domain.entity.City
import kotlinx.coroutines.flow.Flow

interface FavouriteRepository {

    fun getFavouriteCities(userId: Int): Flow<List<City>>

    fun observeIsFavourite(userId: Int, cityId: Int): Flow<Boolean>

    suspend fun addToFavourite(userId: Int, city: City)

    suspend fun removeFromFavourite(userId: Int, cityId: Int)
}