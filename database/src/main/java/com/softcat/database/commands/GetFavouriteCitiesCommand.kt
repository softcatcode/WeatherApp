package com.softcat.database.commands

import com.softcat.database.managers.local.region.RegionManager
import com.softcat.database.managers.remote.favourites.FavouritesManager
import com.softcat.database.model.CityDbModel

class GetFavouriteCitiesCommand(
    private val userId: Int,
    private val favouritesManager: FavouritesManager,
    private val regionManager: RegionManager
): Command {

    var result: Result<List<CityDbModel>>? = null
        private set

    override suspend fun execute() {
        favouritesManager.getFavouriteCitiesIds(userId).onSuccess {
            result = regionManager.getCities(it)
        }.onFailure {
            result = Result.failure(it)
        }
    }

    override suspend fun rollback() {}
}