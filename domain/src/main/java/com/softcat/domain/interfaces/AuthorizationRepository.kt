package com.softcat.domain.interfaces

import com.softcat.domain.entity.User

interface AuthorizationRepository {

    fun enter(login: String, password: String): Result<User>

    suspend fun register(login: String, email: String, password: String): Result<User>
}