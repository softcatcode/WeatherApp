package com.softcat.database.mapper

import android.database.Cursor
import com.softcat.database.model.CityDbModel
import com.softcat.database.model.CountryDbModel

fun toCitiesModels(cursor: Cursor): List<CityDbModel> {
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