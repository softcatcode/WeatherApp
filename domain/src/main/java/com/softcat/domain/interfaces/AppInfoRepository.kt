package com.softcat.domain.interfaces

interface AppInfoRepository {
    fun getSwaggerLink(): String

    fun getServerStatusLink(): String

    fun getStatisticsLink(): String

    fun getDocumentationLink(): String

    fun getAdminPageLink(): String
}