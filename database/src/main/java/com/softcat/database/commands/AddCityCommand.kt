package com.softcat.database.commands

import com.softcat.database.managers.ManagerFactoryInterface
import com.softcat.database.managers.local.region.RegionManager
import com.softcat.database.model.CityDbModel

class AddCityCommand(
    private val model: CityDbModel,
    private val managerFactory: ManagerFactoryInterface
): Command {

    var result: Result<Unit>? = null
        private set

    override suspend fun execute() {
        val regionManager = managerFactory.createRegionManager()
        result = regionManager.saveCity(model)
    }

    override suspend fun rollback() {
        val regionManager = managerFactory.createRegionManager()
        regionManager.deleteCity(model.id)
    }
}