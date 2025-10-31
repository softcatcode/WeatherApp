package com.softcat.data.implementations

import com.softcat.data.network.api.NewWeatherApiService.Companion.BASE_URL
import com.softcat.domain.interfaces.AppInfoRepository
import javax.inject.Inject

class AppInfoRepositoryImpl @Inject constructor(): AppInfoRepository {
    override fun getSwaggerLink(): String {
        return BASE_URL.substring(0, BASE_URL.length - 1)
    }

    override fun getServerStatusLink(): String {
        return BASE_URL + "status"
    }

    override fun getStatisticsLink(): String {
        return BASE_URL
    }

    override fun getDocumentationLink(): String {
        return BASE_URL + "documentation"
    }

    override fun getAdminPageLink(): String {
        return BASE_URL + "admin"
    }
}