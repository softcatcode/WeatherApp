package com.softcat.database.managers.local.weather

import android.icu.util.Calendar
import com.softcat.database.internal.CursorMapperInterface
import com.softcat.database.internal.sqlExecutor.SQLiteInterface
import com.softcat.database.model.CurrentWeatherDbModel
import com.softcat.database.model.WeatherDbModel
import com.softcat.database.model.WeatherTypeDbModel
import java.util.Date
import javax.inject.Inject

class WeatherManagerImpl @Inject constructor(
    private val executor: SQLiteInterface,
    private val mapper: CursorMapperInterface
): WeatherManager {

    override fun addWeather(model: WeatherDbModel): Result<Unit> {
        try {
            executor.deleteWeather(model.cityId, model.timeEpoch)
        } catch (_: Exception) {}

        return try {
            executor.insertWeather(model)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun removeWeather(model: WeatherDbModel): Result<Unit> {
        return try {
            executor.deleteWeather(model.cityId, model.timeEpoch)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun addHourlyWeather(model: CurrentWeatherDbModel): Result<Unit> {
        try {
            executor.deleteCurrentWeather(model.cityId, model.timeEpoch)
        } catch (_: Exception) {}

        return try {
            executor.insertCurrentWeather(model)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun removeHourlyWeather(model: CurrentWeatherDbModel): Result<Unit> {
        return try {
            executor.deleteCurrentWeather(model.cityId, model.timeEpoch)
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

    override fun removeWeatherTypes(types: List<WeatherTypeDbModel>): Result<Unit> {
        return try {
            types.forEach {
                executor.deleteWeatherType(it.code)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getWeatherTypes(typeCodes: List<Int>): Result<List<WeatherTypeDbModel>> {
        return try {
            val result = typeCodes.map { code ->
                val cursor = executor.getWeatherType(code)
                mapper.toWeatherType(cursor)
            }
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getHourlyForecast(
        cityId: Int,
        dayTimeEpoch: Long
    ): Result<List<CurrentWeatherDbModel>> {
        return try {
            val dayEndTime = Calendar.getInstance().apply {
                time = Date(dayTimeEpoch * 1000)
                add(Calendar.DATE, 1)
            }.timeInMillis / 1000L
            val cursor = executor.getCurrentWeather(cityId, dayTimeEpoch, dayEndTime)
            val result = mapper.toCurrentWeatherList(cursor)
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getDaysWeather(cityId: Int, startTime: Long, endTime: Long): Result<List<WeatherDbModel>> {
        return try {
            val cursor = executor.getDaysWeather(cityId, startTime, endTime)
            val result = mapper.toWeatherList(cursor)
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun clearWeatherData(): Result<Unit> {
        return try {
            executor.clearWeatherData()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun weatherTypesLoaded(): Result<Boolean> {
        return try {
            val isLoaded = mapper.unpackBoolean(executor.weatherTypesLoaded())
            Result.success(isLoaded)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}