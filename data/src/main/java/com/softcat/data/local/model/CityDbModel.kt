package com.softcat.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = CityDbModel.TABLE_NAME)
data class CityDbModel(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    val country: String
) {
    companion object {
        const val TABLE_NAME = "favourite_cities"
    }
}