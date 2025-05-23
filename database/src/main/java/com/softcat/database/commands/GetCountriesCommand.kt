package com.softcat.database.commands

import com.softcat.database.exceptions.NoDelayCommandException
import com.softcat.database.managers.local.region.RegionManager
import com.softcat.database.model.CountryDbModel

class GetCountriesCommand(
    private val regionManager: RegionManager
): Command {

    var result: Result<List<CountryDbModel>>? = null
        private set

    override suspend fun execute() {
        result = regionManager.getCountries()
    }

    override suspend fun rollback() {
        throw NoDelayCommandException()
    }
}