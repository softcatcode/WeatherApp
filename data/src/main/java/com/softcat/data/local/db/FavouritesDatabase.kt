package com.softcat.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.softcat.data.local.model.CityDbModel

@Database(entities = [CityDbModel::class], version = 1, exportSchema = false)
abstract class FavouritesDatabase: RoomDatabase() {

    abstract fun getCitiesDao(): FavouriteCitiesDao

    companion object {

        private const val NAME = "FavouritesDatabase"
        private val LOCK = Any()
        private var INSTANCE: FavouritesDatabase? = null

        fun getInstance(context: Context): FavouritesDatabase {
            INSTANCE?.let { return it }
            synchronized(LOCK) {
                INSTANCE?.let { return it }
                val db = Room.databaseBuilder(context, FavouritesDatabase::class.java, NAME).build()
                INSTANCE = db
                return db
            }
        }
    }
}