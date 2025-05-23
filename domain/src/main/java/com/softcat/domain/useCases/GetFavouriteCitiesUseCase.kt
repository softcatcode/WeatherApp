package com.softcat.domain.useCases

import com.softcat.domain.entity.City
import com.softcat.domain.interfaces.FavouriteRepository
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import javax.inject.Inject

class GetFavouriteCitiesUseCase @Inject constructor(
    private val repository: FavouriteRepository
) {
    operator fun invoke(userId: String): Flow<List<City>> {
        Timber.i("${this::class.simpleName} invoked")
        return repository.getFavouriteCities(userId)
    }
}