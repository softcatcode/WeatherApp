package com.softcat.weatherapp

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.softcat.data.implementations.DatastoreRepositoryImpl
import com.softcat.weatherapp.MockCreator.getDataStoreMock
import com.softcat.weatherapp.TestDataCreator.getCityName
import com.softcat.weatherapp.TestDataCreator.getTestUser
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.reset

@RunWith(AndroidJUnit4::class)
class DatastoreRepositoryTest {

    private val datastore = getDataStoreMock()
    private val repository = DatastoreRepositoryImpl(datastore)

    @Before
    fun resetMocks() {
       reset(datastore)
    }

    @Test
    fun saveAndGetCity() = runBlocking {
        val savedCity = getCityName()

        repository.saveCityToDatastore(savedCity)
        val city1 = repository.getLastCityFromDatastore()
        val city2 = repository.getLastCityFromDatastore()

        assert(city1 == savedCity)
        assert(city2 == savedCity)
    }

    @Test
    fun saveAndGetUser() = runBlocking {
        val savedUser = getTestUser()

        repository.saveLastUser(savedUser)
        val user1 = repository.getLastUser()
        val user2 = repository.getLastUser()

        assert(user1 == savedUser)
        assert(user2 == savedUser)
    }
}