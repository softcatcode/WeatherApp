package com.softcat.database.commands

import com.softcat.database.managers.local.weather.WeatherManager
import com.softcat.database.model.CurrentWeatherDbModel

class AddCurrentWeatherCommand(
    private val weatherDbModel: CurrentWeatherDbModel,
    private val manager: WeatherManager
): Command {

    var result: Result<Unit>? = null
        private set

    override suspend fun execute() {
        result = manager.addHourlyWeather(weatherDbModel)
    }

    override suspend fun rollback() {
        TODO("Not yet implemented")
    }
}