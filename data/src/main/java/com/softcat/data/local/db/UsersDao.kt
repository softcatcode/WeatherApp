package com.softcat.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.softcat.data.local.model.UserDbModel

@Dao
interface UsersDao {

    @Query("select ${UserDbModel.TABLE_NAME}.name from ${UserDbModel.TABLE_NAME} where 1")
    fun getUserNames(): List<String>

    @Query("select * from ${UserDbModel.TABLE_NAME} where name = :name and password = :password")
    fun authorize(name: String, password: String): List<UserDbModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun register(model: UserDbModel)
}