package com.softcat.database.managers.remote.favourites

interface FavouritesManager {

    suspend fun addToFavourites(userId: Int, cityId: Int): Result<Unit>

    suspend fun removeFromFavourites(userId: Int, cityId: Int): Result<Unit>

    suspend fun getFavouriteCitiesIds(userId: Int): Result<List<Int>>

    suspend fun isFavourite(userId: Int, cityId: Int): Result<Boolean>
}