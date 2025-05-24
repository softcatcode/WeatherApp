package com.softcat.data.implementations

import com.softcat.database.exceptions.UnknownException
import com.softcat.database.exceptions.UserVerificationException
import com.softcat.data.mapper.toEntity
import com.softcat.data.mapper.userDbModel
import com.softcat.database.facade.DatabaseFacade
import com.softcat.domain.entity.User
import com.softcat.domain.interfaces.AuthorizationRepository
import javax.inject.Inject

class AuthorizationRepositoryImpl @Inject constructor(
    private val database: DatabaseFacade
): AuthorizationRepository {

    override suspend fun enter(login: String, password: String): Result<User> {
        val result = database.verifyUser(login, password)
        result.onSuccess {
            val user = it.toEntity()
            return Result.success(user)
        }.onFailure {
            return Result.failure(it)
        }
        return Result.failure(UserVerificationException(login))
    }

    override suspend fun register(login: String, email: String, password: String): Result<User> {
        val user = userDbModel(login, email, password)
        val result = database.createUser(user)
        result.onSuccess {
            val userModel = it.toEntity()
            return Result.success(userModel)
        }.onFailure {
            return Result.failure(it)
        }
        return Result.failure(UnknownException("register function unexpectedly failed"))
    }
}