package com.softcat.data.implementations

import com.softcat.domain.entity.City
import com.softcat.domain.interfaces.FavouriteRepository
import com.softcat.data.local.db.FavouriteCitiesDao
import com.softcat.data.mapper.toDbModel
import com.softcat.data.mapper.toEntities
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

class FavouriteRepositoryImpl @Inject constructor(
    private val citiesDao: FavouriteCitiesDao
) : FavouriteRepository {

    override fun getFavouriteCities(): Flow<List<City>> {
        Timber.i("${this::class.simpleName}.getFavouriteCities()")
        return citiesDao.getFavouriteCities().map {
            Timber.i("FETCHED: $it")
            it.toEntities()
        }
    }

    override fun observeIsFavourite(cityId: Int): Flow<Boolean> {
        Timber.i("${this::class.simpleName}.observeIsFavourite")
        return citiesDao.observeIdFavourite(cityId)
    }

    override suspend fun addToFavourite(city: City) {
        Timber.i("${this::class.simpleName}.addToFavourite($city)")
        citiesDao.addToFavourites(city.toDbModel())
    }

    override suspend fun removeFromFavourite(cityId: Int) {
        Timber.i("${this::class.simpleName}.removeFromFavourite($cityId)")
        citiesDao.removeCity(cityId)
    }
}