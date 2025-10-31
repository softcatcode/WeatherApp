package com.softcat.domain.useCases

import com.softcat.domain.entity.WebPageType
import com.softcat.domain.interfaces.AppInfoRepository
import timber.log.Timber
import javax.inject.Inject

class GetWebPageLinkUseCase @Inject constructor(
    private val repository: AppInfoRepository
) {
    operator fun invoke(type: WebPageType): String {
        Timber.i("${this::class.simpleName} invoked")
        when (type) {
            WebPageType.SwaggerUI -> repository.getSwaggerLink()
            WebPageType.Documentation -> repository.getDocumentationLink()
            WebPageType.Admin -> repository.getAdminPageLink()
            WebPageType.Statistics -> repository.getStatisticsLink()
            WebPageType.ServerStatus -> repository.getServerStatusLink()
        }
        return repository.getSwaggerLink()
    }
}