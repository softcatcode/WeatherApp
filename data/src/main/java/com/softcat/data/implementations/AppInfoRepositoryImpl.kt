package com.softcat.data.implementations

import com.softcat.data.network.api.NewWeatherApiService
import com.softcat.domain.interfaces.AppInfoRepository
import javax.inject.Inject

class AppInfoRepositoryImpl @Inject constructor(): AppInfoRepository {
    override fun getSwaggerLink(): String {
        return "${NewWeatherApiService.BASE_URL}swagger"
    }
}