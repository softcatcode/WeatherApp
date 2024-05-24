package com.softcat.weatherapp.domain.useCases

import com.softcat.weatherapp.domain.interfaces.FavouriteRepository
import javax.inject.Inject

class RemoveFromFavouriteUseCase @Inject constructor(
    private val repository: FavouriteRepository
) {
    suspend operator fun invoke(cityId: Int) = repository.removeFromFavourite(cityId)
}