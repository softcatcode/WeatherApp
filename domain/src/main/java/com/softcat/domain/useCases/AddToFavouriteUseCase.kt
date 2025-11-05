package com.softcat.domain.useCases

import com.softcat.domain.entity.City
import com.softcat.domain.interfaces.FavouriteRepository
import timber.log.Timber
import javax.inject.Inject

class AddToFavouriteUseCase @Inject constructor(
    private val repository: FavouriteRepository
) {
    suspend operator fun invoke(userId: String, city: City): Result<Unit> {
        Timber.i("${this::class.simpleName} invoked")
        return try {
            repository.addToFavourite(userId, city)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}