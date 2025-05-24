package com.softcat.database.managers.remote.user

import com.softcat.database.model.UserDbModel

interface UsersManager {

    suspend fun loadUserList(): List<UserDbModel>

    suspend fun signIn(user: UserDbModel): Result<UserDbModel>

    suspend fun logIn(name: String, password: String): Result<UserDbModel>
}