package com.softcat.database.internal

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.softcat.database.internal.queries.CreateQueries
import com.softcat.database.internal.queries.DeleteQueries
import com.softcat.database.internal.queries.IndexQueries
import javax.inject.Inject

class DbHelper @Inject constructor(
    context: Context
): SQLiteOpenHelper(
    context,
    DatabaseRules.DATABASE_NAME,
    null,
    DatabaseRules.VERSION + 5
) {

    private fun createTables(db: SQLiteDatabase) {
        with (db) {
            execSQL(CreateQueries.CREATE_COUNTRIES)
            execSQL(CreateQueries.CREATE_CITIES)
            execSQL(CreateQueries.CREATE_WEATHER_TYPES)
            execSQL(CreateQueries.CREATE_WEATHER)
            execSQL(CreateQueries.CREATE_CURRENT_WEATHER)
            execSQL(CreateQueries.CREATE_PLOT_TABLE)
        }
    }

    private fun dropTables(db: SQLiteDatabase) {
        with (db) {
            execSQL(DeleteQueries.DROP_CITIES)
            execSQL(DeleteQueries.DROP_COUNTRIES)
            execSQL(DeleteQueries.DROP_WEATHER_TYPES)
            execSQL(DeleteQueries.DROP_CURRENT_WEATHER)
            execSQL(DeleteQueries.DROP_WEATHER)
            execSQL(DeleteQueries.DROP_PLOTS)
        }
    }

    private fun createIndexes(db: SQLiteDatabase) {
        with (db) {
            execSQL(IndexQueries.CREATE_COUNTRY_INDEX)
            execSQL(IndexQueries.CREATE_PLOT_INDEX)
            execSQL(IndexQueries.CREATE_WEATHER_INDEX)
            execSQL(IndexQueries.CREATE_CURRENT_WEATHER_INDEX)
        }
    }

    override fun onCreate(db: SQLiteDatabase?) {
        if (db != null) {
            createTables(db)
            createIndexes(db)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.let {
            dropTables(it)
        }
        onCreate(db)
    }
}