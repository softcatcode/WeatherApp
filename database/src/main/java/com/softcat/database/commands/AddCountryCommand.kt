package com.softcat.database.commands

import com.softcat.database.managers.ManagerFactoryInterface
import com.softcat.database.managers.local.region.RegionManager
import com.softcat.database.model.CountryDbModel

class AddCountryCommand(
    private val model: CountryDbModel,
    private val managerFactory: ManagerFactoryInterface
): Command {

    var result: Result<Int>? = null
        private set

    override suspend fun execute() {
        val regionManager = managerFactory.createRegionManager()
        result = regionManager.saveCountry(model)
    }

    override suspend fun rollback() {
        val regionManager = managerFactory.createRegionManager()
        regionManager.deleteCountry(model.id)
    }
}