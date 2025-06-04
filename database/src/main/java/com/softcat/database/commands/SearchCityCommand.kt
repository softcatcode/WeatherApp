package com.softcat.database.commands

import com.softcat.database.exceptions.NoDelayCommandException
import com.softcat.database.managers.local.region.RegionManager
import com.softcat.database.model.CityDbModel

class SearchCityCommand(
    private val regionManager: RegionManager,
    private val query: String
): Command {

    var result: Result<List<CityDbModel>>? = null
        private set

    override suspend fun execute() {
        result = regionManager.searchCity(query)
    }

    override suspend fun rollback() {
        throw NoDelayCommandException()
    }
}