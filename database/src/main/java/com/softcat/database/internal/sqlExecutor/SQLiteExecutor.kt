package com.softcat.database.internal.sqlExecutor

import android.content.ContentValues
import android.database.Cursor
import com.softcat.database.internal.DatabaseRules
import com.softcat.database.internal.DbHelper
import com.softcat.database.internal.queries.DeleteQueries
import com.softcat.database.internal.queries.DeleteQueries.DELETE_CITY
import com.softcat.database.internal.queries.DeleteQueries.DELETE_COUNTRY
import com.softcat.database.internal.queries.DeleteQueries.DELETE_CURRENT_WEATHER
import com.softcat.database.internal.queries.DeleteQueries.DELETE_WEATHER
import com.softcat.database.internal.queries.DeleteQueries.DELETE_WEATHER_TYPE
import com.softcat.database.internal.queries.GetDataQueries
import com.softcat.database.internal.queries.GetDataQueries.GET_WEATHER_TYPE
import com.softcat.database.internal.queries.InsertQueries
import com.softcat.database.internal.queries.InsertQueries.INSERT_CITY
import com.softcat.database.internal.queries.InsertQueries.INSERT_COUNTRY
import com.softcat.database.internal.queries.InsertQueries.INSERT_CURRENT_WEATHER
import com.softcat.database.internal.queries.InsertQueries.INSERT_WEATHER
import com.softcat.database.model.CityDbModel
import com.softcat.database.model.CountryDbModel
import com.softcat.database.model.CurrentWeatherDbModel
import com.softcat.database.model.PlotDbModel
import com.softcat.database.model.WeatherDbModel
import com.softcat.database.model.WeatherTypeDbModel
import java.util.Locale
import javax.inject.Inject

class SQLiteExecutor @Inject constructor(
    private val dbHelper: DbHelper
): SQLiteInterface {

    override fun getPlots(userId: String): Cursor {
        val query = GetDataQueries.GET_USER_PLOTS.format(userId)
        return dbHelper.readableDatabase.rawQuery(query, null)
    }

    override fun insertPlot(model: PlotDbModel) {
        val query = InsertQueries.INSERT_PLOT.format(
            model.id,
            model.parameter,
            model.values,
            model.time,
            model.cityId,
            model.authorId,
            model.description
        )
        dbHelper.writableDatabase.execSQL(query)
    }

    override fun deletePlot(plotId: Int) {
        val query = DeleteQueries.DELETE_PLOT.format(plotId)
        dbHelper.writableDatabase.execSQL(query)
    }

    override fun getCities(): Cursor {
        return dbHelper.readableDatabase.rawQuery(GetDataQueries.GET_CITIES, null)
    }

    override fun getCountries(): Cursor {
        return dbHelper.readableDatabase.rawQuery(GetDataQueries.GET_COUNTRIES, null)
    }

    override fun getCountryId(name: String): Cursor {
        val query = GetDataQueries.GET_COUNTRY_ID.format(name)
        return dbHelper.readableDatabase.rawQuery(query, null)
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
            INSERT_CITY.format(Locale.US, id, name, countryId, latitude, longitude)
        }
        dbHelper.writableDatabase.execSQL(query)
    }

    override fun insertWeatherType(model: WeatherTypeDbModel) {
        val values = with (model) {
            ContentValues().apply {
                put("code", code)
                put("dayDescription", dayDescription)
                put("nightDescription", nightDescription)
                put("url", url)
                put("icon", iconBytes)
            }
        }
        dbHelper.writableDatabase.insert(
            DatabaseRules.WEATHER_TYPE_TABLE_NAME,
            null,
            values
        )
    }

    override fun deleteWeatherType(code: Int) {
        dbHelper.writableDatabase.execSQL(DELETE_WEATHER_TYPE.format(code))
    }

    override fun getWeatherType(code: Int): Cursor {
        val query = GET_WEATHER_TYPE.format(code)
        return dbHelper.writableDatabase.rawQuery(query, null)
    }

    override fun insertWeather(model: WeatherDbModel) {
        val query = with (model) {
            INSERT_WEATHER.format(
                Locale.US,
                timeEpoch, cityId, type, avgTemp, humidity,
                windSpeed, snowVolume, precipitations, vision,
                sunriseTime, sunsetTime, moonriseTime, moonsetTime,
                moonIllumination, moonPhase,
                rainChance,
            )
        }
        dbHelper.writableDatabase.execSQL(query)
    }

    override fun insertCurrentWeather(model: CurrentWeatherDbModel) {
        val query = with (model) {
            INSERT_CURRENT_WEATHER.format(
                Locale.US,
                cityId, timeEpoch, tempC, feelsLike, isDay, type,
                windSpeed, precipitations, snow, humidity, cloud, vision
            )
        }
        dbHelper.writableDatabase.execSQL(query)
    }

    override fun deleteWeather(cityId: Int, timeEpoch: Long) {
        val query = DELETE_WEATHER.format(cityId, timeEpoch)
        dbHelper.writableDatabase.execSQL(query)
    }

    override fun deleteCurrentWeather(cityId: Int, timeEpoch: Long) {
        val query = DELETE_CURRENT_WEATHER.format(cityId, timeEpoch)
        dbHelper.writableDatabase.execSQL(query)
    }

    override fun getCurrentWeather(cityId: Int, start: Long, end: Long): Cursor {
        val query = GetDataQueries.GET_CURRENT_WEATHER.format(cityId, start, end)
        return dbHelper.readableDatabase.rawQuery(query, null)
    }

    override fun getDaysWeather(cityId: Int, start: Long, end: Long): Cursor {
        val query = GetDataQueries.GET_WEATHER.format(cityId, start, end)
        return dbHelper.readableDatabase.rawQuery(query, null)
    }
}