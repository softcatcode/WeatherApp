package com.softcat.database.commands

import com.softcat.database.managers.local.weather.WeatherManager
import com.softcat.database.model.WeatherDbModel

class AddWeatherCommand(
    private val weatherDbModel: WeatherDbModel,
    private val manager: WeatherManager
): Command {

    var result: Result<Unit>? = null
        private set

    override suspend fun execute() {
        result = manager.addWeather(weatherDbModel)
    }

    override suspend fun rollback() {
        TODO("Not yet implemented")
    }
}