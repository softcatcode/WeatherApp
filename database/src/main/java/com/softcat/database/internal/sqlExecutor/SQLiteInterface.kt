package com.softcat.database.internal.sqlExecutor

import android.database.Cursor
import com.softcat.database.model.CityDbModel
import com.softcat.database.model.CountryDbModel
import com.softcat.database.model.CurrentWeatherDbModel
import com.softcat.database.model.WeatherDbModel
import com.softcat.database.model.WeatherTypeDbModel
import com.softcat.domain.entity.CurrentWeather

interface SQLiteInterface {
    fun getCities(): Cursor

    fun getCountries(): Cursor

    fun getCity(id: Int): Cursor

    fun insertCountry(model: CountryDbModel): Cursor

    fun deleteCountry(id: Int)

    fun deleteCity(id: Int)

    fun insertCity(model: CityDbModel)

    fun insertWeatherType(model: WeatherTypeDbModel)

    fun insertWeather(model: WeatherDbModel)

    fun insertCurrentWeather(model: CurrentWeatherDbModel)

    fun deleteWeather(cityId: Int, timeEpoch: Long)

    fun deleteCurrentWeather(cityId: Int, timeEpoch: Long)
}