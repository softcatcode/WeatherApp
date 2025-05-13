package com.softcat.database.commands

import com.softcat.database.exceptions.NoDelayCommandException
import com.softcat.database.managers.remote.favourites.FavouritesManager

class IsFavouriteCommand(
    private val userId: Int,
    private val cityId: Int,
    private val favouritesManager: FavouritesManager
): Command {

    var result: Result<Boolean>? = null
        private set

    override suspend fun execute() {
        result = favouritesManager.isFavourite(userId, cityId)
    }

    override suspend fun rollback() {
        throw NoDelayCommandException()
    }
}