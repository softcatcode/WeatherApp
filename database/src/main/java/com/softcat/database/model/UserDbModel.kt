package com.softcat.database.model

data class UserDbModel(
    val name: String,
    val email: String,
    val password: String,
    val role: String,
    val registerTimeEpoch: Long,
    val id: String = UNSPECIFIED_ID,
) {
    companion object {
        const val ROLE_REGULAR = "Regular"
        const val ROLE_FOLLOWER = "Follower"
        const val ROLE_PREMIUM = "Premium"
        const val UNSPECIFIED_ID = "unspecified"
    }
}