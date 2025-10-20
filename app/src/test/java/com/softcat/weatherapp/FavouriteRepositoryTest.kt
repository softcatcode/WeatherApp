package com.softcat.weatherapp

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.softcat.data.implementations.FavouriteRepositoryImpl
import com.softcat.weatherapp.MockCreator.getDatabaseMock
import com.softcat.weatherapp.TestDataCreator.getCityList
import com.softcat.weatherapp.TestDataCreator.getRandomCity
import com.softcat.weatherapp.TestDataCreator.getRandomUserId
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.reset

@RunWith(AndroidJUnit4::class)
class FavouriteRepositoryTest {

    private val database = getDatabaseMock()
    private val repository = FavouriteRepositoryImpl(database)

    @Before
    fun resetMocks() {
        reset(database)
    }

    @Test
    fun addToFavourites() = runBlocking {
        val userId = getRandomUserId()
        val savedCity = getRandomCity()

        repository.addToFavourite(userId, savedCity)
        val answer1 = repository.observeIsFavourite(userId, savedCity.id).first()
        val answer2 = repository.observeIsFavourite(userId + 1, savedCity.id).first()
        val answer3 = repository.observeIsFavourite(userId, savedCity.id + 1).first()
        val answer4 = repository.observeIsFavourite(userId, savedCity.id).first()
        repository.removeFromFavourite(userId, savedCity.id)

        assert(answer1)
        assert(!answer2)
        assert(!answer3)
        assert(answer4)
    }

    @Test
    fun doubleAddToFavourites() = runBlocking {
        val userId = getRandomUserId()
        val savedCity = getRandomCity()

        repository.addToFavourite(userId, savedCity)
        repository.addToFavourite(userId, savedCity)
        val answer1 = repository.observeIsFavourite(userId, savedCity.id).first()
        val answer2 = repository.observeIsFavourite(userId + 1, savedCity.id).first()
        val answer3 = repository.observeIsFavourite(userId, savedCity.id + 1).first()
        val answer4 = repository.observeIsFavourite(userId, savedCity.id).first()
        repository.removeFromFavourite(userId, savedCity.id)

        assert(answer1)
        assert(!answer2)
        assert(!answer3)
        assert(answer4)
    }

    @Test
    fun removeFromFavourites() = runBlocking {
        val userId = getRandomUserId()
        val savedCity = getRandomCity()

        repository.addToFavourite(userId, savedCity)
        repository.removeFromFavourite(userId, savedCity.id)
        val answer1 = repository.observeIsFavourite(userId, savedCity.id).first()
        val answer2 = repository.observeIsFavourite(userId + 1, savedCity.id).first()
        val answer3 = repository.observeIsFavourite(userId, savedCity.id + 1).first()
        val answer4 = repository.observeIsFavourite(userId, savedCity.id).first()

        assert(!answer1)
        assert(!answer2)
        assert(!answer3)
        assert(!answer4)
    }

    @Test
    fun getFavouriteCities() = runBlocking {
        val userId = getRandomUserId()
        val cities = getCityList()

        cities.forEach { city ->
            repository.addToFavourite(userId, city)
        }
        val favourites = repository.getFavouriteCities(userId).first()
        cities.forEach { city ->
            repository.removeFromFavourite(userId, city.id)
        }

        assert(cities.size == favourites.size)
        for (i in cities.indices) {
            assert(cities[i] in favourites)
        }
    }

    @Test
    fun getFavouriteCitiesUnknownUser() = runBlocking {
        val userId = getRandomUserId()
        val cities = getCityList()

        cities.forEach { city ->
            repository.addToFavourite(userId, city)
        }
        val favourites = repository.getFavouriteCities(userId + 1).first()
        cities.forEach { city ->
            repository.removeFromFavourite(userId, city.id)
        }

        assert(favourites.isEmpty())
    }

    @Test
    fun getFavouriteCitiesWhenEmpty() = runBlocking {
        val userId = getRandomUserId()
        val cities = getCityList()

        cities.forEach { city ->
            repository.addToFavourite(userId, city)
        }
        cities.forEach { city ->
            repository.removeFromFavourite(userId, city.id)
        }
        val favourites = repository.getFavouriteCities(userId).first()

        assert(favourites.isEmpty())
    }

    @Test
    fun getFavouriteCitiesWhenTwoOfThreeRemoved() = runBlocking {
        val userId = getRandomUserId()
        val cities = getCityList()

        cities.forEach { city ->
            repository.addToFavourite(userId, city)
        }
        repository.removeFromFavourite(userId, cities[0].id)
        repository.removeFromFavourite(userId, cities[1].id)
        val favourites = repository.getFavouriteCities(userId).first()
        cities.forEach { city ->
            repository.removeFromFavourite(userId, city.id)
        }

        assert(favourites.size == 1)
        assert(favourites[0] == cities[2])
    }
}