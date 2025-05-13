package com.softcat.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val id: Int,
    val name: String,
    val role: Status,
    val email: String,
    val password: String
): Parcelable {
    enum class Status {
        Regular, Follower, Premium
    }
}