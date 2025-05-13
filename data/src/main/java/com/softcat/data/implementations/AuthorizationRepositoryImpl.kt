package com.softcat.data.implementations

import com.softcat.database.exceptions.UnknownException
import com.softcat.database.exceptions.UserVerificationException
import com.softcat.database.mapper.toEntity
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
        val result = database.createUser(login, email, password)
        result.onSuccess {
            val user = it.toEntity()
            return Result.success(user)
        }.onFailure {
            return Result.failure(it)
        }
        return Result.failure(UnknownException("register function unexpectedly failed"))
    }
}