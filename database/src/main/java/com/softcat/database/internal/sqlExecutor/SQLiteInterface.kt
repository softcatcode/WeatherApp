package com.softcat.database.internal.sqlExecutor

import android.database.Cursor
import com.softcat.database.model.CityDbModel
import com.softcat.database.model.CountryDbModel
import com.softcat.database.model.CurrentWeatherDbModel
import com.softcat.database.model.PlotDbModel
import com.softcat.database.model.WeatherDbModel
import com.softcat.database.model.WeatherTypeDbModel

interface SQLiteInterface {

    fun getPlots(userId: String): Cursor

    fun insertPlot(model: PlotDbModel)

    fun deletePlot(plotId: Int)

    fun getCities(): Cursor

    fun getCountries(): Cursor

    fun getCountryId(name: String): Cursor

    fun getCity(id: Int): Cursor

    fun insertCountry(model: CountryDbModel): Cursor

    fun deleteCountry(id: Int)

    fun deleteCity(id: Int)

    fun insertCity(model: CityDbModel)

    fun insertWeatherType(model: WeatherTypeDbModel)

    fun deleteWeatherType(code: Int)

    fun getWeatherType(code: Int): Cursor

    fun insertWeather(model: WeatherDbModel)

    fun insertCurrentWeather(model: CurrentWeatherDbModel)

    fun deleteWeather(cityId: Int, timeEpoch: Long)

    fun deleteCurrentWeather(cityId: Int, timeEpoch: Long)

    fun getCurrentWeather(cityId: Int, start: Long, end: Long): Cursor

    fun getDaysWeather(cityId: Int, start: Long, end: Long): Cursor

    fun clearWeatherData()

    fun weatherTypesLoaded(): Cursor
}