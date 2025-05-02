package com.softcat.domain.entity

data class User(
    val id: Int,
    val name: String,
    val role: Status
) {
    enum class Status {
        Regular, Follower, Premium
    }
}