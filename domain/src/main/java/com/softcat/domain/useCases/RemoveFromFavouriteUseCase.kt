package com.softcat.domain.useCases

import com.softcat.domain.interfaces.FavouriteRepository
import timber.log.Timber
import javax.inject.Inject

class RemoveFromFavouriteUseCase @Inject constructor(
    private val repository: FavouriteRepository
) {
    suspend operator fun invoke(userId: String, cityId: Int): Result<Unit> {
        Timber.i("${this::class.simpleName} invoked")
        return try {
            repository.removeFromFavourite(userId, cityId)
            Result.success(Unit)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}