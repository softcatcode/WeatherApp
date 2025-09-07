package com.softcat.weatherapp

import androidx.test.platform.app.InstrumentationRegistry
import com.softcat.data.implementations.DatastoreRepositoryImpl
import com.softcat.domain.interfaces.DatastoreRepository
import com.softcat.weatherapp.TestDataCreator.getCityName
import com.softcat.weatherapp.TestDataCreator.getTestUser
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class DatastoreRepositoryTest {

    private val applicationContext = InstrumentationRegistry
        .getInstrumentation().targetContext.applicationContext

    private val application = applicationContext as WeatherApplication

    private val component = DaggerUnitTestsComponent
        .factory().create(applicationContext, application.dataStore)

    private lateinit var repository: DatastoreRepository

    @Before
    fun initRepository() {
        repository = component.getDatastoreRepository()
    }

    @Test
    fun saveAndGetCity() = runBlocking {
        val repository = DatastoreRepositoryImpl(application.dataStore)
        val savedCity = getCityName()

        repository.saveCityToDatastore(savedCity)
        val city1 = repository.getLastCityFromDatastore()
        val city2 = repository.getLastCityFromDatastore()

        assert(city1 == savedCity)
        assert(city2 == savedCity)
    }

    @Test
    fun saveAndGetUser() = runBlocking {
        val repository = DatastoreRepositoryImpl(application.dataStore)
        val savedUser = getTestUser()

        repository.saveLastUser(savedUser)
        val user1 = repository.getLastUser()
        val user2 = repository.getLastUser()

        assert(user1 == savedUser)
        assert(user2 == savedUser)
    }
}