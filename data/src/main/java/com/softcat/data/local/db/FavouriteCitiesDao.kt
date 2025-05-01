package com.softcat.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.softcat.data.local.model.CityDbModel
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouriteCitiesDao {

    @Query("select * from ${CityDbModel.TABLE_NAME}")
    fun getFavouriteCities(): Flow<List<CityDbModel>>

    @Query("select exists (select * from ${CityDbModel.TABLE_NAME} where id = :cityId limit 1)")
    fun observeIdFavourite(cityId: Int): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToFavourites(model: CityDbModel)

    @Query("delete from ${CityDbModel.TABLE_NAME} where id = :cityId")
    suspend fun removeCity(cityId: Int)
}