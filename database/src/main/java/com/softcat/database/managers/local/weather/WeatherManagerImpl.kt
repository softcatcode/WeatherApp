package com.softcat.database.managers.local.weather

import android.icu.util.Calendar
import com.softcat.database.internal.sqlExecutor.SQLiteExecutor
import com.softcat.database.mapper.toCurrentWeatherList
import com.softcat.database.mapper.toWeatherList
import com.softcat.database.mapper.toWeatherType
import com.softcat.database.model.CurrentWeatherDbModel
import com.softcat.database.model.WeatherDbModel
import com.softcat.database.model.WeatherTypeDbModel
import java.util.Date
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

    override fun getWeatherTypes(typeCodes: List<Int>): Result<List<WeatherTypeDbModel>> {
        return try {
            val result = typeCodes.map { code ->
                val cursor = executor.getWeatherType(code)
                toWeatherType(cursor)
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
            val calendar = Calendar.getInstance().apply {
                time = Date(dayTimeEpoch)
                set(Calendar.MILLISECONDS_IN_DAY, 0)
                add(Calendar.DATE, 1)
            }
            val cursor = executor.getCurrentWeather(cityId, dayTimeEpoch, calendar.timeInMillis)
            val result = toCurrentWeatherList(cursor)
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getDaysWeather(cityId: Int, startTime: Long, endTime: Long): Result<List<WeatherDbModel>> {
        return try {
            val cursor = executor.getDaysWeather(cityId, startTime, endTime)
            val result = toWeatherList(cursor)
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}