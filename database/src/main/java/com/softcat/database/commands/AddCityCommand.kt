package com.softcat.database.commands

import com.softcat.database.managers.local.region.RegionManager
import com.softcat.database.model.CityDbModel

class AddCityCommand(
    private val model: CityDbModel,
    private val regionManager: RegionManager
): Command {

    var result: Result<Unit>? = null
        private set

    override suspend fun execute() {
        result = regionManager.saveCity(model)
    }

    override suspend fun rollback() {
        regionManager.deleteCity(model.id)
    }
}