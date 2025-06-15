package com.softcat.data.mapper

import com.softcat.database.model.UserDbModel
import com.softcat.domain.entity.User
import java.util.Calendar

fun UserDbModel.toEntity() = User(
    id = id,
    name = name,
    role = role.toUserRole(),
    email = email,
    password = password,
)

private fun String.toUserRole() = when (this) {
    UserDbModel.ROLE_REGULAR -> User.Status.Regular
    UserDbModel.ROLE_FOLLOWER -> User.Status.Follower
    UserDbModel.ROLE_PREMIUM -> User.Status.Premium
    else -> User.Status.Regular
}

private fun User.Status.toDbModel() = when (this) {
    User.Status.Regular -> UserDbModel.ROLE_REGULAR
    User.Status.Follower -> UserDbModel.ROLE_FOLLOWER
    User.Status.Premium -> UserDbModel.ROLE_PREMIUM
}

fun userDbModel(login: String, email: String, password: String): UserDbModel {
    return UserDbModel(
        name = login,
        email = email,
        password = password,
        role = User.Status.Regular.toDbModel(),
        registerTimeEpoch = Calendar.getInstance().timeInMillis / 1000L
    )
}