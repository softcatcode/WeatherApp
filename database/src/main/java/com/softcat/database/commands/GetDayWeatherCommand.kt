package com.softcat.database.commands

import com.softcat.database.exceptions.NoDelayCommandException
import com.softcat.database.managers.local.weather.WeatherManager
import com.softcat.database.model.WeatherDbModel

class GetDayWeatherCommand(
    private val cityId: Int,
    private val startMillis: Long,
    private val endMillis: Long,
    private val weatherManager: WeatherManager
): Command {

    var result: Result<List<WeatherDbModel>>? = null
        private set

    override suspend fun execute() {
        result = weatherManager.getDaysWeather(cityId, startMillis, endMillis)
    }

    override suspend fun rollback() {
        throw NoDelayCommandException()
    }
}