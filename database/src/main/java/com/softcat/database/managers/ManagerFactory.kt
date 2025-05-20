package com.softcat.database.managers

import com.softcat.database.internal.sqlExecutor.SQLiteExecutor
import com.softcat.database.managers.local.region.RegionManager
import com.softcat.database.managers.local.region.RegionManagerImpl
import com.softcat.database.managers.local.region.RegionManagerRemoteImpl
import com.softcat.domain.useCases.Lab9SettingsUseCase
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class ManagerFactory @Inject constructor(
    private val settingsUseCase: Lab9SettingsUseCase,
    private val executor: SQLiteExecutor,
): ManagerFactoryInterface {
    override suspend fun createRegionManager(): RegionManager {
        val isLocal = settingsUseCase.isLocal()
        return if (isLocal)
            RegionManagerImpl(executor)
        else
            RegionManagerRemoteImpl()
    }
}