package com.softcat.domain.useCases

import com.softcat.domain.interfaces.FavouriteRepository
import javax.inject.Inject

class GetFavouriteCitiesUseCase @Inject constructor(
    private val repository: FavouriteRepository
) {
    operator fun invoke() = repository.getFavouriteCities()
}