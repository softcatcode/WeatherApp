package com.softcat.database.commands

import com.softcat.database.exceptions.NoDelayCommandException
import com.softcat.database.managers.local.region.RegionManager
import com.softcat.database.model.CountryDbModel

class UpdateCountriesCommand(
    private val countries: List<CountryDbModel>,
    private val regionManager: RegionManager,
): Command {

    var result: Result<List<Int>>? = null
        private set

    override suspend fun execute() {
        result = regionManager.updateCountries(countries)
    }

    override suspend fun rollback() {
        throw NoDelayCommandException()
    }
}