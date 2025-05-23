package com.softcat.database.managers.local.init

import com.softcat.database.model.WeatherTypeDbModel

interface InitializeDbManager {

    fun isInitialized(): Result<Boolean>

    fun initialize(
        types: List<WeatherTypeDbModel>,
    ): Result<Unit>
}