package com.softcat.database.commands

import com.softcat.database.exceptions.NoDelayCommandException
import com.softcat.database.managers.local.weather.WeatherManager
import com.softcat.database.model.WeatherTypeDbModel

class GetWeatherTypesCommand(
    private val typeCodes: List<Int>,
    private val manager: WeatherManager
): Command {

    var result: Result<List<WeatherTypeDbModel>>? = null
        private set

    override suspend fun execute() {
        result = manager.getWeatherTypes(typeCodes)
    }

    override suspend fun rollback() {
        throw NoDelayCommandException()
    }
}