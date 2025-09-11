package com.softcat.weatherapp

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.softcat.data.implementations.AuthorizationRepositoryImpl
import com.softcat.database.exceptions.AuthorizationFailedException
import com.softcat.database.facade.DatabaseFacade
import com.softcat.database.model.UserDbModel
import com.softcat.weatherapp.MockCreator.getDatabaseMock
import com.softcat.weatherapp.TestDataCreator.getTestUser
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Mockito.reset
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.mockito.kotlin.times
import org.mockito.kotlin.verify

@RunWith(AndroidJUnit4::class)
class AuthorizationRepositoryTest {

    private val database: DatabaseFacade = getDatabaseMock()
    private val repository = AuthorizationRepositoryImpl(database)

    @Before
    fun resetMocks() {
        reset(database)
    }

    @Test
    fun enterRegistered() = runBlocking {
        val user = getTestUser()
        val userModel = UserDbModel(
            name = user.name,
            email = user.email,
            password = user.password,
            role = user.role.name,
            registerTimeEpoch = 1757400828,
            id = user.id
        )
        `when`(database.verifyUser(user.name, user.password)).thenReturn(Result.success(userModel))

        val result = repository.enter(user.name, user.password)

        verify(database.verifyUser(user.name, user.password), times(1))
        assert(result.isSuccess)
        assert(result.getOrThrow() == user)
    }

    @Test
    fun enterUnregistered() = runBlocking {
        val user = getTestUser()
        `when`(database.verifyUser(user.name, user.password))
            .thenReturn(Result.failure(AuthorizationFailedException(user.name, user.password)))

        val result = repository.enter(user.name, user.password)

        verify(database.verifyUser(user.name, user.password), times(1))
        assert(result.isFailure)
        assert(result.exceptionOrNull() == AuthorizationFailedException(user.name, user.password))
    }

    @Test
    fun register() = runBlocking {
        val user = getTestUser()
        `when`(database.createUser(any())).thenAnswer { invocation ->
            Result.success(invocation.arguments.first() as UserDbModel)
        }
        val captor = ArgumentCaptor.forClass(UserDbModel::class.java)

        val result = repository.register(user.name, user.email, user.password)

        verify(database.createUser(captor.capture()))
        with (captor.value) {
            assert(id == user.id)
            assert(name == user.name)
            assert(password == user.password)
            assert(email == user.email)
            assert(role == user.role.name)
        }
        assert(result.isSuccess)
        assert(result.getOrThrow() == user)
    }
}