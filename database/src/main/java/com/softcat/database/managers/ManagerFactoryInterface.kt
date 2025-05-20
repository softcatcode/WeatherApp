package com.softcat.database.managers

import com.softcat.database.managers.local.region.RegionManager

interface ManagerFactoryInterface {
    suspend fun createRegionManager(): RegionManager
}