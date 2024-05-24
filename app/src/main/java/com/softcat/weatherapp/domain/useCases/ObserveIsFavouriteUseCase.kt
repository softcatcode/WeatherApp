package com.softcat.weatherapp.domain.useCases

import com.softcat.weatherapp.domain.interfaces.FavouriteRepository
import javax.inject.Inject

class ObserveIsFavouriteUseCase @Inject constructor(
    private val repository: FavouriteRepository
) {
    operator fun invoke(cityId: Int) = repository.observeIsFavourite(cityId)
}