package com.softcat.data.implementations

import com.softcat.data.network.api.NewWeatherApiService.Companion.BASE_URL
import com.softcat.domain.interfaces.AppInfoRepository
import javax.inject.Inject

class AppInfoRepositoryImpl @Inject constructor(): AppInfoRepository {

    private val baseUrl = BASE_URL.substring(0, BASE_URL.length - "api/v1/".length)

    override fun getSwaggerLink(): String {
        return baseUrl + "api/v1"
    }

    override fun getServerStatusLink(): String {
        return baseUrl + "status"
    }

    override fun getStatisticsLink(): String {
        return baseUrl
    }

    override fun getDocumentationLink(): String {
        return baseUrl + "documentation"
    }

    override fun getAdminPageLink(): String {
        return baseUrl + "admin"
    }

    override fun getUsefulLinksPage(): String {
        return baseUrl + "management"
    }
}