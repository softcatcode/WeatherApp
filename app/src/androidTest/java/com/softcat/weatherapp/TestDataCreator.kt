package com.softcat.weatherapp

import com.softcat.domain.entity.User

object TestDataCreator {
    fun getCityName() = "London"

    fun getTestUser() = User(
        id = "0495128357",
        name = "Bill",
        role = User.Status.Premium,
        email = "billy@gmail.com",
        password = "1_Pw90fVC"
    )
}