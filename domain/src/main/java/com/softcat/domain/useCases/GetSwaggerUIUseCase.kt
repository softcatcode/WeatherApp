package com.softcat.domain.useCases

import com.softcat.domain.interfaces.AppInfoRepository
import timber.log.Timber
import javax.inject.Inject

class GetSwaggerUIUseCase @Inject constructor(
    private val repository: AppInfoRepository
) {
    fun getSwaggerLink(): String {
        Timber.i("${this::class.simpleName} invoked")
        return repository.getSwaggerLink()
    }
}