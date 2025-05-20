package com.softcat.database.internal

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.softcat.database.internal.queries.CreateQueries
import com.softcat.database.internal.queries.DeleteQueries
import javax.inject.Inject

class DbHelper @Inject constructor(
    context: Context
): SQLiteOpenHelper(
    context,
    DatabaseRules.DATABASE_NAME,
    null,
    DatabaseRules.VERSION
) {

    private fun createTables(db: SQLiteDatabase) {
        with (db) {
            execSQL(CreateQueries.CREATE_COUNTRIES)
            execSQL(CreateQueries.CREATE_CITIES)
            execSQL(CreateQueries.CREATE_WEATHER_TYPES)
            execSQL(CreateQueries.CREATE_WEATHER)
            execSQL(CreateQueries.CREATE_CURRENT_WEATHER)
        }
    }

    private fun dropTables(db: SQLiteDatabase) {
        with (db) {
            execSQL(DeleteQueries.DROP_CITIES)
            execSQL(DeleteQueries.DROP_COUNTRIES)
        }
    }

    override fun onCreate(db: SQLiteDatabase?) {
        if (db != null)
            createTables(db)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.let {
            dropTables(it)
            createTables(it)
        }
        db?.execSQL(DeleteQueries.DROP_DATABASE)
        onCreate(db)
    }
}