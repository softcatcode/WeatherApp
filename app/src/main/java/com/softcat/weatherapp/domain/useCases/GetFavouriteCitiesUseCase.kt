package com.softcat.weatherapp.domain.useCases

import com.softcat.weatherapp.domain.interfaces.FavouriteRepository
import javax.inject.Inject

class GetFavouriteCitiesUseCase @Inject constructor(
    private val repository: FavouriteRepository
) {
    operator fun invoke() = repository.favouriteCities
}