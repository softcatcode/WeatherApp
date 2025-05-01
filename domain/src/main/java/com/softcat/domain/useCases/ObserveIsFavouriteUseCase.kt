package com.softcat.domain.useCases

import com.softcat.domain.interfaces.FavouriteRepository
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import javax.inject.Inject

class ObserveIsFavouriteUseCase @Inject constructor(
    private val repository: FavouriteRepository
) {
    operator fun invoke(cityId: Int): Flow<Boolean> {
        Timber.i("${this::class.simpleName} invoked")
        return repository.observeIsFavourite(cityId)
    }
}