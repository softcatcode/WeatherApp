package com.softcat.data.implementations

import com.softcat.domain.entity.City
import com.softcat.domain.interfaces.FavouriteRepository
import com.softcat.data.mapper.toEntities
import com.softcat.database.facade.DatabaseFacade
import com.softcat.database.model.CountryDbModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

class FavouriteRepositoryImpl @Inject constructor(
    private val database: DatabaseFacade
) : FavouriteRepository {
    private var selectedUserAndCity: Pair<String, Int>? = null
    private val isFavouriteUpdate = MutableSharedFlow<Unit>(replay = 1)

    private suspend fun isFavourite(): Boolean {
        Timber.i("${this::class.simpleName}.isFavourite")
        selectedUserAndCity?.let { value ->
            val (userId, cityId) = value
            database.isFavourite(userId, cityId).onSuccess {
                return it
            }
        }
        return false
    }

    override fun observeIsFavourite(userId: String, cityId: Int): Flow<Boolean> {
        Timber.i("${this::class.simpleName}.observeIsFavourite($userId, $cityId)")
        selectedUserAndCity = userId to cityId
        return flow {
            isFavouriteUpdate.emit(Unit)
            isFavouriteUpdate.collect {
                emit(isFavourite())
            }
        }
    }

    private var selectedUser: String? = null
    private val favouriteCitiesUpdate = MutableSharedFlow<Unit>(replay = 1)

    private suspend fun loadFavouriteCities(): List<City> {
        Timber.i("${this::class.simpleName}.loadFavouriteCities")
        selectedUser?.let { value ->
            database.getFavouriteCities(value).onSuccess { cityModels ->
                database.getCountries().onSuccess { countryModels ->
                    return cityModels.toEntities(countryModels)
                }
            }
        }
        return emptyList()
    }

    override fun getFavouriteCities(userId: String): Flow<List<City>> {
        Timber.i("${this::class.simpleName}.getFavouriteCitiesIds($userId)")
        selectedUser = userId
        return flow {
            favouriteCitiesUpdate.emit(Unit)
            favouriteCitiesUpdate.collect {
                emit(loadFavouriteCities())
            }
        }
    }

    override suspend fun addToFavourite(userId: String, city: City) {
        Timber.i("${this::class.simpleName}.addToFavourites($userId, $city)")
        val countryModel = CountryDbModel(name = city.country)
        database.saveCountry(countryModel)
        database.addToFavourites(userId, city.id)
        isFavouriteUpdate.emit(Unit)
        favouriteCitiesUpdate.emit(Unit)
    }

    override suspend fun removeFromFavourite(userId: String, cityId: Int) {
        Timber.i("${this::class.simpleName}.removeFromFavourite($userId, $cityId)")
        database.removeFromFavourites(userId, cityId)
        isFavouriteUpdate.emit(Unit)
        favouriteCitiesUpdate.emit(Unit)
    }
}