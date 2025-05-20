package com.softcat.database.mapper

import com.softcat.database.model.UserDbModel
import com.softcat.database.model.WeatherTypeDbModel
import com.softcat.domain.entity.User
import com.softcat.domain.entity.WeatherTypeInfo
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

fun UserDbModel.toEntity() = User(
    id = id,
    name = name,
    role = User.Status.valueOf(role),
    email = email,
    password = password
)

fun userDbModel(id: Int, login: String, email: String, password: String): UserDbModel {
    val time = Calendar.getInstance().time
    val locale = Locale.getDefault()
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", locale)
    val timeFormat = SimpleDateFormat("hh-mm-ss", locale)
    val regDate = dateFormat.format(time)
    val regTime = timeFormat.format(time)
    return UserDbModel(
        id = id,
        name = login,
        email = email,
        password = password,
        role = User.Status.Regular.name,
        registerDate = regDate,
        registerTime = regTime,
    )
}
