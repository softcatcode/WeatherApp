package com.softcat.weatherapp.data.implementations

import com.softcat.weatherapp.data.local.db.FavouriteCitiesDao
import com.softcat.weatherapp.data.mapper.toDbModel
import com.softcat.weatherapp.data.mapper.toEntities
import com.softcat.weatherapp.domain.entity.City
import com.softcat.weatherapp.domain.interfaces.FavouriteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FavouriteRepositoryImpl @Inject constructor(
    private val citiesDao: FavouriteCitiesDao
) : FavouriteRepository {

    override fun getFavouriteCities() = citiesDao.getFavouriteCities().map { it.toEntities() }

    override fun observeIsFavourite(cityId: Int) = citiesDao.observeIdFavourite(cityId)

    override suspend fun addToFavourite(city: City) = citiesDao.addToFavourites(city.toDbModel())

    override suspend fun removeFromFavourite(cityId: Int) = citiesDao.removeCity(cityId)
}