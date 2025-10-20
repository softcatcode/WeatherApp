package com.softcat.weatherapp

import com.softcat.data.implementations.AuthorizationRepositoryImpl
import com.softcat.database.exceptions.AuthorizationFailedException
import com.softcat.database.facade.DatabaseFacade
import com.softcat.database.model.UserDbModel
import com.softcat.weatherapp.MockCreator.getDatabaseMock
import com.softcat.weatherapp.TestDataCreator.getRandomUser
import com.softcat.weatherapp.TestDataCreator.getRandomUserDbModel
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.anyString
import org.mockito.Mockito.reset
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.mockito.kotlin.times
import org.mockito.kotlin.verify

class AuthorizationRepositoryTest {

    private val database: DatabaseFacade = getDatabaseMock()
    private val repository = AuthorizationRepositoryImpl(database)

    @Before
    fun resetMocks() {
        reset(database)
    }

    @Test
    fun enterRegistered(): Unit = runBlocking {
        val user = getRandomUser()
        val userModel = UserDbModel(
            name = user.name,
            email = user.email,
            password = user.password,
            role = user.role.name,
            registerTimeEpoch = 1757400828,
            id = user.id
        )
        `when`(database.verifyUser(anyString(), anyString())).thenReturn(Result.success(userModel))

        val result = repository.enter(user.name, user.password)

        verify(database, times(1))
            .verifyUser(anyString(), anyString())
        assert(result.isSuccess)
        assert(result.getOrThrow() == user)
    }

    @Test
    fun enterUnregistered(): Unit = runBlocking {
        val user = getRandomUser()
        `when`(database.verifyUser(user.name, user.password))
            .thenReturn(Result.failure(AuthorizationFailedException(user.name, user.password)))

        val result = repository.enter(user.name, user.password)

        verify(database, times(1))
            .verifyUser(user.name, user.password)
        assert(result.isFailure)
        assert(result.exceptionOrNull()?.message == AuthorizationFailedException(user.name, user.password).message)
    }

    @Test
    fun register(): Unit = runBlocking {
        val user = getRandomUserDbModel()
        `when`(database.createUser(any())).thenReturn(Result.success(user))

        val result = repository.register(user.name, user.email, user.password)

        assert(result.isSuccess)
        with (result.getOrThrow()) {
            assert(name == user.name)
            assert(password == user.password)
            assert(email == user.email)
            assert(role.name == user.role)
        }
    }
}