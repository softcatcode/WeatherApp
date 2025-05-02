package com.softcat.domain.useCases

import com.softcat.domain.entity.User
import com.softcat.domain.interfaces.AuthorizationRepository
import timber.log.Timber
import javax.inject.Inject

class AuthorizationUseCase @Inject constructor(
    private val repository: AuthorizationRepository
) {
    fun logIn(login: String, password: String): Result<User> {
        Timber.i("${this::class.simpleName} invoked")
        return repository.enter(login, password)
    }

    suspend fun signIn(login: String, password: String): Result<User> {
        Timber.i("${this::class.simpleName} invoked")
        return repository.register(login, password)
    }
}