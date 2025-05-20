package com.softcat.database.internal.sqlExecutor

import android.database.Cursor
import com.softcat.database.internal.DbHelper
import com.softcat.database.internal.queries.DeleteQueries.DELETE_CITY
import com.softcat.database.internal.queries.DeleteQueries.DELETE_COUNTRY
import com.softcat.database.internal.queries.GetDataQueries
import com.softcat.database.internal.queries.InsertQueries.INSERT_CITY
import com.softcat.database.internal.queries.InsertQueries.INSERT_COUNTRY
import com.softcat.database.internal.queries.InsertQueries.INSERT_WEATHER_TYPE
import com.softcat.database.model.CityDbModel
import com.softcat.database.model.CountryDbModel
import com.softcat.database.model.WeatherDbModel
import com.softcat.database.model.WeatherTypeDbModel
import javax.inject.Inject

class SQLiteExecutor @Inject constructor(
    private val dbHelper: DbHelper
): SQLiteInterface {
    override fun getCities(): Cursor {
        return dbHelper.readableDatabase.rawQuery(GetDataQueries.GET_CITIES, null)
    }

    override fun getCountries(): Cursor {
        return dbHelper.readableDatabase.rawQuery(GetDataQueries.GET_COUNTRIES, null)
    }

    override fun getCity(id: Int): Cursor {
        val query = GetDataQueries.GET_CITY.format(id)
        return dbHelper.readableDatabase.rawQuery(query, null)
    }

    override fun insertCountry(model: CountryDbModel): Cursor {
        dbHelper.writableDatabase.execSQL(INSERT_COUNTRY.format(model.name))
        val query = GetDataQueries.GET_COUNTRY_ID.format(model.name)
        return dbHelper.readableDatabase.rawQuery(query, null)
    }

    override fun deleteCountry(id: Int) {
        dbHelper.writableDatabase.execSQL(DELETE_COUNTRY.format(id))
    }

    override fun deleteCity(id: Int) {
        dbHelper.writableDatabase.execSQL(DELETE_CITY.format(id))
    }

    override fun insertCity(model: CityDbModel) {
        val query = with (model) {
            INSERT_CITY.format(id, name, countryId, latitude, longitude)
        }
        dbHelper.writableDatabase.execSQL(query)
    }

    override fun insertWeatherType(model: WeatherTypeDbModel) {
        val query = INSERT_WEATHER_TYPE.format(model.code, model.dayDescription, model.nightDescription)
        dbHelper.writableDatabase.execSQL(query)
    }

    override fun insertWeather(model: WeatherDbModel) {
        TODO("Not yet implemented")
    }
}