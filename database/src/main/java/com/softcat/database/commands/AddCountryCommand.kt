package com.softcat.database.commands

import com.softcat.database.managers.local.region.RegionManager
import com.softcat.database.model.CountryDbModel

class AddCountryCommand(
    private val model: CountryDbModel,
    private val regionManager: RegionManager
): Command {

    var result: Result<Int>? = null
        private set

    override suspend fun execute() {
        result = regionManager.saveCountry(model)
    }

    override suspend fun rollback() {
        regionManager.deleteCountry(model.id)
    }
}