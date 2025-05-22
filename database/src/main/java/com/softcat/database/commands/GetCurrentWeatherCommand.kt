package com.softcat.database.commands

import com.softcat.database.exceptions.NoDelayCommandException
import com.softcat.database.managers.local.weather.WeatherManager
import com.softcat.database.model.CurrentWeatherDbModel

class GetCurrentWeatherCommand(
    private val cityId: Int,
    private val dayTimeEpoch: Long,
    private val weatherManager: WeatherManager
): Command {

    var result: Result<List<CurrentWeatherDbModel>>? = null
        private set

    override suspend fun execute() {
        result = weatherManager.getHourlyForecast(cityId, dayTimeEpoch)
    }

    override suspend fun rollback() {
        throw NoDelayCommandException()
    }
}