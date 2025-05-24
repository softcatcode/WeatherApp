package com.softcat.database.internal

import android.database.Cursor
import com.softcat.database.model.CityDbModel
import com.softcat.database.model.CountryDbModel
import com.softcat.database.model.CurrentWeatherDbModel
import com.softcat.database.model.PlotDbModel
import com.softcat.database.model.WeatherDbModel
import com.softcat.database.model.WeatherTypeDbModel

interface CursorMapperInterface {

    fun toCityModels(cursor: Cursor): List<CityDbModel>

    fun toCountriesModels(cursor: Cursor): List<CountryDbModel>

    fun toInt(cursor: Cursor): Int

    fun toCurrentWeatherList(cursor: Cursor): List<CurrentWeatherDbModel>

    fun toWeatherList(cursor: Cursor): List<WeatherDbModel>

    fun toWeatherType(cursor: Cursor): WeatherTypeDbModel

    fun toPlot(cursor: Cursor): PlotDbModel
}