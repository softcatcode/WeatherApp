package com.softcat.data.mapper

import com.softcat.data.local.model.UserDbModel
import com.softcat.domain.entity.User
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

fun UserDbModel.toEntity() = User(
    id = id,
    name = name,
    role = User.Status.valueOf(role)
)

fun userDbModel(login: String, password: String): UserDbModel {
    val time = Calendar.getInstance().time
    val locale = Locale.getDefault()
    val dateFormat = SimpleDateFormat("yyyy-MM-DD", locale)
    val timeFormat = SimpleDateFormat("hh-mm-ss", locale)
    val regDate = dateFormat.format(time)
    val regTime = timeFormat.format(time)
    return UserDbModel(
        name = login,
        password = password,
        role = User.Status.Regular.name,
        registerDate = regDate,
        registerTime = regTime,
    )
}