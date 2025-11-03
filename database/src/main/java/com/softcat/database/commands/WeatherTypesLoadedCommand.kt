package com.softcat.database.commands

import com.softcat.database.exceptions.NoCommandResultException
import com.softcat.database.exceptions.NoDelayCommandException
import com.softcat.database.managers.local.weather.WeatherManager

class WeatherTypesLoadedCommand(
    private val manager: WeatherManager
): Command {

    var result: Result<Boolean> = Result.failure(NoCommandResultException())
        private set

    override suspend fun execute() {
        result = manager.weatherTypesLoaded()
    }

    override suspend fun rollback() {
        throw NoDelayCommandException()
    }
}