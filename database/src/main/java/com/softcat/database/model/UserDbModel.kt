package com.softcat.database.model

data class UserDbModel(
    val id: Int = UNSPECIFIED_ID,
    val name: String,
    val email: String,
    val password: String,
    val role: String,
    val registerDate: String,
    val registerTime: String,
) {
    companion object {
        const val UNSPECIFIED_ID = 0
    }
}