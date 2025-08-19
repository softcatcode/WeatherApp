package com.softcat.database.commands

import com.softcat.database.exceptions.NoCommandResultException
import com.softcat.database.exceptions.NoDelayCommandException
import com.softcat.database.managers.local.weather.WeatherManager

class ClearWeatherDataCommand(
    private val manager: WeatherManager
): Command {

    var result: Result<Unit> = Result.failure(NoCommandResultException())
        private set

    override suspend fun execute() {
        result = manager.clearWeatherData()
    }

    override suspend fun rollback() {
        throw NoDelayCommandException()
    }
}