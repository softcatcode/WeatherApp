package com.softcat.database.mapper

import android.database.Cursor
import androidx.core.database.getBlobOrNull
import androidx.core.database.getIntOrNull
import com.softcat.database.model.CityDbModel
import com.softcat.database.model.CountryDbModel
import com.softcat.database.model.CurrentWeatherDbModel
import com.softcat.database.model.WeatherDbModel
import com.softcat.database.model.WeatherTypeDbModel

fun toCityModels(cursor: Cursor): List<CityDbModel> {
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

fun toCountriesModels(cursor: Cursor): List<CountryDbModel> {
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

fun toInt(cursor: Cursor): Int {
    cursor.moveToNext()
    val result = cursor.getInt(0)
    cursor.close()
    return result
}

fun toCurrentWeatherList(cursor: Cursor): List<CurrentWeatherDbModel> {
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
                cloud = it.getInt(12),
                vision = it.getFloat(12),
            )
            result.add(item)
        }
        result
    }
}

fun toWeatherList(cursor: Cursor): List<WeatherDbModel> {
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
                sunriseTime = it.getString(10),
                sunsetTime = it.getString(11),
                moonriseTime = it.getString(12),
                moonsetTime = it.getString(13),
                moonIllumination = it.getInt(14),
                moonPhase = it.getString(15),
                rainChance = it.getInt(16)
            )
            result.add(item)
        }
        result
    }
}

fun toWeatherType(cursor: Cursor): WeatherTypeDbModel {
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