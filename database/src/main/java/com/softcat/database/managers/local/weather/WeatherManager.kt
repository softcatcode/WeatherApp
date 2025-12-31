package com.softcat.database.managers.local.weather

import com.softcat.database.model.CurrentWeatherDbModel
import com.softcat.database.model.WeatherDbModel
import com.softcat.database.model.WeatherTypeDbModel

interface WeatherManager {
    fun addWeather(model: WeatherDbModel): Result<Unit>

    fun removeWeather(model: WeatherDbModel): Result<Unit>

    fun addHourlyWeather(model: CurrentWeatherDbModel): Result<Unit>

    fun removeHourlyWeather(model: CurrentWeatherDbModel): Result<Unit>

    fun updateWeatherTypes(types: List<WeatherTypeDbModel>): Result<Unit>

    fun removeWeatherTypes(types: List<WeatherTypeDbModel>): Result<Unit>

    fun getWeatherTypes(typeCodes: List<Int>): Result<List<WeatherTypeDbModel>>

    fun getHourlyForecast(cityId: Int, dayTimeEpoch: Long): Result<List<CurrentWeatherDbModel>>

    fun getDaysWeather(cityId: Int, startTime: Long, endTime: Long): Result<List<WeatherDbModel>>

    fun clearWeatherData(): Result<Unit>

    fun weatherTypesLoaded(): Result<Boolean>
}