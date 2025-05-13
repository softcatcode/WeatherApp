package com.softcat.database.internal.sqlExecutor

import android.database.Cursor
import com.softcat.database.model.CityDbModel
import com.softcat.database.model.CountryDbModel

interface SQLiteInterface {
    fun getCities(): Cursor

    fun getCountries(): Cursor

    fun getCity(id: Int): Cursor

    fun insertCountry(model: CountryDbModel): Cursor

    fun deleteCountry(id: Int)

    fun deleteCity(id: Int)

    fun insertCity(model: CityDbModel)
}