package com.softcat.domain.interfaces

import com.softcat.domain.entity.City
import kotlinx.coroutines.flow.Flow

interface FavouriteRepository {

    fun getFavouriteCities(userId: String): Flow<List<City>>

    fun observeIsFavourite(userId: String, cityId: Int): Flow<Boolean>

    suspend fun addToFavourite(userId: String, city: City)

    suspend fun removeFromFavourite(userId: String, cityId: Int)
}