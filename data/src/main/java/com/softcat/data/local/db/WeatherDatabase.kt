package com.softcat.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.softcat.data.local.model.CityDbModel
import com.softcat.data.local.model.UserDbModel

@Database(entities = [CityDbModel::class, UserDbModel::class], version = 1, exportSchema = false)
abstract class WeatherDatabase: RoomDatabase() {

    abstract fun getCitiesDao(): FavouriteCitiesDao

    abstract fun getUsersDao(): UsersDao

    companion object {

        private val NAME = this::class.simpleName
        private val LOCK = Any()
        private var INSTANCE: WeatherDatabase? = null

        fun getInstance(context: Context): WeatherDatabase {
            INSTANCE?.let { return it }
            synchronized(LOCK) {
                INSTANCE?.let { return it }
                val db = Room.databaseBuilder(context, WeatherDatabase::class.java, NAME).build()
                INSTANCE = db
                return db
            }
        }
    }
}