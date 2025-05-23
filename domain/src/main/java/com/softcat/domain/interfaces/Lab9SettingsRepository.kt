package com.softcat.domain.interfaces

import kotlinx.coroutines.flow.Flow

interface Lab9SettingsRepository {

    suspend fun init()

    suspend fun switchRegionStorage()

    suspend fun isLocalRegionStorage(): Flow<Boolean>

    suspend fun isLocal(): Boolean
}