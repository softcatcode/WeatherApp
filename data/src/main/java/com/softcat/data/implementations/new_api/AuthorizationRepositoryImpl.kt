package com.softcat.data.implementations.new_api

import com.google.gson.Gson
import com.softcat.database.exceptions.UnknownException
import com.softcat.database.exceptions.UserVerificationException
import com.softcat.data.mapper.toEntity
import com.softcat.data.mapper.userDbModel
import com.softcat.data.network.api.NewWeatherApiService
import com.softcat.data.network.dto.ErrorResponseDto
import com.softcat.data.network.dto.UserDto
import com.softcat.database.facade.DatabaseFacade
import com.softcat.domain.entity.User
import com.softcat.domain.interfaces.AuthorizationRepository
import javax.inject.Inject

class AuthorizationRepositoryImpl @Inject constructor(
    private val apiService: NewWeatherApiService
): AuthorizationRepository {

    override suspend fun enter(login: String, password: String): Result<User> {
        return try {
            val response = apiService.logIn(login, password)
            val user = response.toEntity()
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun register(login: String, email: String, password: String): Result<User> {
        return try {
            val userModel = apiService.createUser(login, email, password)
            val user = userModel.toEntity()
            return Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}