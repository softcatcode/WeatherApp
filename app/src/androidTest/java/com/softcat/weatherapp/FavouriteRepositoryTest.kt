package com.softcat.weatherapp

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.softcat.weatherapp.data.implementations.FavouriteRepositoryImpl
import com.softcat.weatherapp.domain.entity.City
import com.softcat.weatherapp.domain.interfaces.FavouriteRepository
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.random.Random

@RunWith(AndroidJUnit4::class)
class FavouriteRepositoryTest {

    private val application =
        InstrumentationRegistry
                .getInstrumentation()
                .targetContext
                .applicationContext as WeatherApplication

    private val component = DaggerUnitTestsComponent.factory().create(application)
    private val daoInterface = component.getFavouriteCitiesDao()

    @Test
    fun notStatedCity() = runBlocking {
        val repository: FavouriteRepository = FavouriteRepositoryImpl(daoInterface)
        val flow = repository.observeIsFavourite(Random.nextInt(100, 900))
        val answer = flow.first()
        assertFalse(answer)
    }

    @Test
    fun simpleAddCityCheck() = runBlocking {
        val repository: FavouriteRepository = FavouriteRepositoryImpl(daoInterface)
        val city = City(
            id = Random.nextInt(100, 300),
            name = "Moscow",
            country = "Russia"
        )
        repository.addToFavourite(city)
        val answer = repository.observeIsFavourite(city.id).first()
        val otherCityId = Random.nextInt(300, 500)
        val otherCityAnswer = repository.observeIsFavourite(otherCityId).first()

        assertTrue(answer)
        assertFalse(otherCityAnswer)
    }

    @Test
    fun deleteAbsentCityAndCheckIt() = runBlocking {
        val repository: FavouriteRepository = FavouriteRepositoryImpl(daoInterface)
        val cityId = Random.nextInt(100, 300)
        repository.removeFromFavourite(cityId)
        val answer = repository.observeIsFavourite(cityId).first()
        val otherCityId = Random.nextInt(300, 500)
        val otherCityAnswer = repository.observeIsFavourite(otherCityId).first()

        assertFalse(answer)
        assertFalse(otherCityAnswer)
    }

    @Test
    fun deleteAndAddCity() = runBlocking {
        val repository: FavouriteRepository = FavouriteRepositoryImpl(daoInterface)
        val city = City(
            id = Random.nextInt(10, 20),
            name = "Moscow",
            country = "Russia"
        )
        repository.removeFromFavourite(city.id)
        repository.addToFavourite(city)
        val answer = repository.observeIsFavourite(city.id).first()
        val otherCityId = Random.nextInt(20, 30)
        val otherCityAnswer = repository.observeIsFavourite(otherCityId).first()

        assertTrue(answer)
        assertFalse(otherCityAnswer)
    }

    @Test
    fun doubleAdding() = runBlocking {
        val repository: FavouriteRepository = FavouriteRepositoryImpl(daoInterface)
        val city = City(
            id = Random.nextInt(30, 40),
            name = "Moscow",
            country = "Russia"
        )
        repository.addToFavourite(city)
        repository.addToFavourite(city)
        val answer = repository.observeIsFavourite(city.id).first()
        val otherCityId = Random.nextInt(40, 50)
        val otherCityAnswer = repository.observeIsFavourite(otherCityId).first()

        assertTrue(answer)
        assertFalse(otherCityAnswer)
    }

    @Test
    fun addTwoCitiesAndCheckThem() = runBlocking {
        val repository: FavouriteRepository = FavouriteRepositoryImpl(daoInterface)
        val firstCity = City(
            id = Random.nextInt(50, 60),
            name = "Moscow",
            country = "Russia"
        )
        val secondCity = City(
            id = Random.nextInt(60, 70),
            name = "London",
            country = "England"
        )
        repository.addToFavourite(firstCity)
        repository.addToFavourite(secondCity)
        val answer1 = repository.observeIsFavourite(firstCity.id).first()
        val answer2 = repository.observeIsFavourite(secondCity.id).first()

        assertTrue(answer1)
        assertTrue(answer2)
    }
}