package com.softcat.data.implementations

import com.softcat.data.local.AuthorizationFailedException
import com.softcat.data.local.UserExistsException
import com.softcat.data.local.db.UsersDao
import com.softcat.data.mapper.toEntity
import com.softcat.data.mapper.userDbModel
import com.softcat.domain.entity.User
import com.softcat.domain.interfaces.AuthorizationRepository
import javax.inject.Inject

class AuthorizationRepositoryImpl @Inject constructor(
    private val userDao: UsersDao
): AuthorizationRepository {
    override fun enter(login: String, password: String): Result<User> {
        val users = userDao.authorize(login, password)
        val user = users.firstOrNull()?.toEntity()
        return if (user == null)
            Result.failure(AuthorizationFailedException(login, password))
        else
            Result.success(user)
    }

    override suspend fun register(login: String, password: String): Result<User> {
        val names = userDao.getUserNames()
        if (login !in names) {
            val model = userDbModel(login, password)
            userDao.register(model)
            return enter(login, password)
        }
        return Result.failure(UserExistsException(login))
    }
}