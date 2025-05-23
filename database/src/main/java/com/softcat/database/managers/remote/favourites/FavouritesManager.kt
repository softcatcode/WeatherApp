package com.softcat.database.managers.remote.favourites

interface FavouritesManager {

    suspend fun addToFavourites(userId: String, cityId: Int): Result<Unit>

    suspend fun removeFromFavourites(userId: String, cityId: Int): Result<Unit>

    suspend fun getFavouriteCitiesIds(userId: String): Result<List<Int>>

    suspend fun isFavourite(userId: String, cityId: Int): Result<Boolean>
}