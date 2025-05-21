package com.softcat.database.managers.local.weather

import com.softcat.database.internal.sqlExecutor.SQLiteExecutor
import com.softcat.database.model.CurrentWeatherDbModel
import com.softcat.database.model.WeatherDbModel
import com.softcat.database.model.WeatherTypeDbModel
import javax.inject.Inject

class WeatherManagerImpl @Inject constructor(
    private val executor: SQLiteExecutor
): WeatherManager {

    override fun addWeather(model: WeatherDbModel): Result<Unit> {
        return try {
            executor.deleteWeather(model.cityId, model.timeEpoch)
            executor.insertWeather(model)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun addHourlyWeather(model: CurrentWeatherDbModel): Result<Unit> {
        return try {
            executor.deleteWeather(model.cityId, model.timeEpoch)
            executor.insertCurrentWeather(model)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun updateWeatherTypes(types: List<WeatherTypeDbModel>): Result<Unit> {
        return try {
            types.forEach {
                executor.insertWeatherType(it)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}