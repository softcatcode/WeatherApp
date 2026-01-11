package com.softcat.domain.useCases

import com.softcat.domain.entity.User
import com.softcat.domain.interfaces.AuthorizationRepository
import com.softcat.domain.interfaces.DatastoreRepository
import timber.log.Timber
import javax.inject.Inject

class AuthorizationUseCase @Inject constructor(
    private val repository: AuthorizationRepository,
    private val datastoreRepository: DatastoreRepository
) {
    suspend fun logIn(login: String, password: String): Result<User> {
        Timber.i("${this::class.simpleName}.logIn invoked")
        return repository.enter(login, password)
    }

    suspend fun register(login: String, email: String, password: String): Result<User> {
        Timber.i("${this::class.simpleName}.register invoked")
        return repository.register(login, email, password)
    }

    suspend fun rememberUser(user: User) {
        Timber.i("${this::class.simpleName}.rememberUser invoked")
        return datastoreRepository.saveLastUser(user)
    }

    suspend fun getLastUser(): User? {
        Timber.i("${this::class.simpleName}.getLastUser invoked")
        return datastoreRepository.getLastUser()
    }

    suspend fun exit(): Result<Unit> {
        Timber.i("${this::class.simpleName}.exit invoked")
        return try {
            Result.success(datastoreRepository.clearLastUser())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}