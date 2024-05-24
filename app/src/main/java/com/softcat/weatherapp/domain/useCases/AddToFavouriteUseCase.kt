package com.softcat.weatherapp.domain.useCases

import com.softcat.weatherapp.domain.entity.City
import com.softcat.weatherapp.domain.interfaces.FavouriteRepository
import javax.inject.Inject

class AddToFavouriteUseCase @Inject constructor(
    private val repository: FavouriteRepository
) {
    suspend operator fun invoke(city: City) = repository.addToFavourite(city)
}