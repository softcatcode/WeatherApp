package com.softcat.domain.useCases

import com.softcat.domain.interfaces.FavouriteRepository
import timber.log.Timber
import javax.inject.Inject

class RemoveFromFavouriteUseCase @Inject constructor(
    private val repository: FavouriteRepository
) {
    suspend operator fun invoke(userId: Int, cityId: Int) {
        Timber.i("${this::class.simpleName} invoked")
        return repository.removeFromFavourite(userId, cityId)
    }
}