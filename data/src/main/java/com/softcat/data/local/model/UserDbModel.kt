package com.softcat.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = UserDbModel.TABLE_NAME)
data class UserDbModel(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val password: String,
    val role: String,
    val registerDate: String,
    val registerTime: String,
) {
    companion object {
        const val TABLE_NAME = "users"
    }
}