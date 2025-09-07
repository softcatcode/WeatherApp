package com.softcat.weatherapp

import com.softcat.domain.entity.City
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

    fun getUserId() = "user-38517-ttt"

    fun getTestCity() = City(
        id = 1852,
        name = "London",
        country = "England",
        latitude = 5.26f,
        longitude = -14f
    )

    fun getCityList() = listOf(
        City(
            id = 1852,
            name = "Moscow",
            country = "England",
            latitude = 5.26f,
            longitude = -14f
        ),
        City(
            id = 1852,
            name = "London",
            country = "England",
            latitude = -3.51f,
            longitude = 0f
        ),
        City(
            id = 1852,
            name = "Amsterdam",
            country = "Netherlands",
            latitude = 0.236f,
            longitude = 80.5f
        )
    )
}