package com.softcat.database.commands

import com.softcat.database.managers.remote.favourites.FavouritesManager

class RemoveFromFavouritesCommand(
    private val userId: String,
    private val cityId: Int,
    private val favouritesManager: FavouritesManager
): Command {

    var result: Result<Unit>? = null
        private set

    override suspend fun execute() {
        result = favouritesManager.removeFromFavourites(userId, cityId)
    }

    override suspend fun rollback() {
        result = favouritesManager.addToFavourites(userId, cityId)
    }
}