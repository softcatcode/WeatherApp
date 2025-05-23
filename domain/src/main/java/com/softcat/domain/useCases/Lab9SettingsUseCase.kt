package com.softcat.domain.useCases

import com.softcat.domain.interfaces.Lab9SettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class Lab9SettingsUseCase @Inject constructor(
    private val repository: Lab9SettingsRepository
) {
    suspend fun init() {
        repository.init()
    }

    suspend fun switchRegionStorage() {
        repository.switchRegionStorage()
    }

    suspend fun isLocalRegionStorage(): Flow<Boolean> {
        return repository.isLocalRegionStorage()
    }

    suspend fun isLocal(): Boolean {
        return repository.isLocal()
    }
}