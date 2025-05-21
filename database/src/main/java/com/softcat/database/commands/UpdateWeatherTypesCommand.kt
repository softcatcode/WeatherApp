package com.softcat.database.commands

import com.softcat.database.exceptions.NoDelayCommandException
import com.softcat.database.managers.local.weather.WeatherManager
import com.softcat.database.model.WeatherTypeDbModel

class UpdateWeatherTypesCommand(
    private val weatherTypes: List<WeatherTypeDbModel>,
    private val manager: WeatherManager
): Command {

    var result: Result<Unit>? = null
        private set

    override suspend fun execute() {
        result = manager.updateWeatherTypes(weatherTypes)
    }

    override suspend fun rollback() {
        throw NoDelayCommandException()
    }
}