package com.softcat.database.mapper

import com.softcat.database.model.UserDbModel
import com.softcat.domain.entity.User
import java.util.Calendar

fun UserDbModel.toEntity() = User(
    id = id,
    name = name,
    role = User.Status.valueOf(role),
    email = email,
    password = password,
)

fun userDbModel(login: String, email: String, password: String): UserDbModel {
    return UserDbModel(
        name = login,
        email = email,
        password = password,
        role = User.Status.Regular.name,
        registerTimeEpoch = Calendar.getInstance().timeInMillis / 1000L
    )
}
