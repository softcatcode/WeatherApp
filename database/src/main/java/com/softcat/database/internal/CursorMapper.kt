package com.softcat.database.internal

import android.database.Cursor
import androidx.core.database.getBlobOrNull
import androidx.core.database.getIntOrNull
import com.softcat.database.model.CityDbModel
import com.softcat.database.model.CountryDbModel
import com.softcat.database.model.CurrentWeatherDbModel
import com.softcat.database.model.PlotDbModel
import com.softcat.database.model.WeatherDbModel
import com.softcat.database.model.WeatherTypeDbModel
import javax.inject.Inject

class CursorMapper @Inject constructor(): CursorMapperInterface {

    override fun toCityModels(cursor: Cursor): List<CityDbModel> {
        val cities = mutableListOf<CityDbModel>()
        cursor.use {
            while (it.moveToNext()) {
                val model = CityDbModel(
                    id = it.getInt(0),
                    name = it.getString(1),
                    countryId = it.getInt(2),
                    latitude = it.getFloat(3),
                    longitude = it.getFloat(4),
                )
                cities.add(model)
            }
        }
        return cities
    }

    override fun toCountryModels(cursor: Cursor): List<CountryDbModel> {
        val countries = mutableListOf<CountryDbModel>()
        cursor.use {
            while (it.moveToNext()) {
                val model = CountryDbModel(
                    id = it.getInt(0),
                    name = it.getString(1),
                )
                countries.add(model)
            }
        }
        return countries
    }

    override fun toInt(cursor: Cursor): Int {
        cursor.moveToNext()
        val result = cursor.getInt(0)
        cursor.close()
        return result
    }

    override fun toCurrentWeatherList(cursor: Cursor): List<CurrentWeatherDbModel> {
        return cursor.use {
            val result = mutableListOf<CurrentWeatherDbModel>()
            while (it.moveToNext()) {
                val item = CurrentWeatherDbModel(
                    id = it.getInt(0),
                    cityId = it.getInt(1),
                    timeEpoch = it.getLong(2),
                    tempC = it.getFloat(3),
                    feelsLike = it.getInt(4),
                    isDay = it.getInt(5),
                    type = it.getInt(6),
                    windSpeed = it.getInt(7),
                    precipitations = it.getInt(8),
                    snow = it.getIntOrNull(9),
                    humidity = it.getInt(10),
                    cloud = it.getInt(11),
                    vision = it.getFloat(12),
                )
                result.add(item)
            }
            result
        }
    }

    override fun toWeatherList(cursor: Cursor): List<WeatherDbModel> {
        return cursor.use {
            val result = mutableListOf<WeatherDbModel>()
            while (it.moveToNext()) {
                val item = WeatherDbModel(
                    id = it.getInt(0),
                    timeEpoch = it.getLong(1),
                    cityId = it.getInt(2),
                    type = it.getInt(3),
                    avgTemp = it.getFloat(4),
                    humidity = it.getInt(5),
                    windSpeed = it.getInt(6),
                    snowVolume = it.getInt(7),
                    precipitations = it.getInt(8),
                    vision = it.getFloat(9),
                    sunriseTime = it.getLong(10),
                    sunsetTime = it.getLong(11),
                    moonriseTime = it.getLong(12),
                    moonsetTime = it.getLong(13),
                    moonIllumination = it.getInt(14),
                    moonPhase = it.getString(15),
                    rainChance = it.getInt(16)
                )
                result.add(item)
            }
            result
        }
    }

    override fun toWeatherType(cursor: Cursor): WeatherTypeDbModel {
        cursor.moveToNext()
        return with (cursor) {
            WeatherTypeDbModel(
                code = getInt(0),
                dayDescription = getString(1),
                nightDescription = getString(2),
                url = getString(3),
                iconBytes = getBlobOrNull(4)
            )
        }
    }

    override fun toPlots(cursor: Cursor): List<PlotDbModel> {
        return cursor.use {
            val result = mutableListOf<PlotDbModel>()
            while (cursor.moveToNext()) {
                val model = with(cursor) {
                    PlotDbModel(
                        id = getInt(0),
                        parameter = getString(1),
                        values = getString(2),
                        time = getString(3),
                        cityId = getInt(4),
                        authorId = getString(5),
                        description = getString(6)
                    )
                }
                result.add(model)
            }
            result
        }
    }

    override fun unpackBoolean(cursor: Cursor): Boolean {
        return cursor.use {
            if (!cursor.moveToNext())
                false
            else
                cursor.getInt(0) == 1
        }
    }
}