package com.softcat.database.commands

import com.softcat.database.managers.remote.favourites.FavouritesManager

class AddToFavouritesCommand(
    private val userId: Int,
    private val cityId: Int,
    private val favouritesManager: FavouritesManager
): Command {

    var result: Result<Unit>? = null
        private set

    override suspend fun execute() {
        result = favouritesManager.addToFavourites(userId, cityId)
    }

    override suspend fun rollback() {
        result = favouritesManager.removeFromFavourites(userId, cityId)
    }
}