package com.softcat.domain.useCases

import com.softcat.domain.entity.City
import com.softcat.domain.interfaces.FavouriteRepository
import javax.inject.Inject

class AddToFavouriteUseCase @Inject constructor(
    private val repository: FavouriteRepository
) {
    suspend operator fun invoke(city: City) = repository.addToFavourite(city)
}