package com.softcat.weatherapp

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.softcat.domain.interfaces.DatastoreRepository
import com.softcat.weatherapp.MockCreator.getDataStoreMock
import com.softcat.weatherapp.MockCreator.getDatabaseMock
import com.softcat.weatherapp.MockCreator.getDocsApiMock
import com.softcat.weatherapp.MockCreator.getWeatherApiMock
import com.softcat.weatherapp.TestDataCreator.getCityName
import com.softcat.weatherapp.TestDataCreator.getTestUser
import com.softcat.weatherapp.di.components.DaggerUnitTestsComponent
import com.softcat.weatherapp.di.components.UnitTestsComponent
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DatastoreRepositoryTest {

    private val context = InstrumentationRegistry
        .getInstrumentation().targetContext.applicationContext

    private lateinit var component: UnitTestsComponent
    private lateinit var repository: DatastoreRepository

    @Before
    fun initRepository() {
        component = DaggerUnitTestsComponent.factory()
            .create(context, getDataStoreMock(), getDatabaseMock(), getDocsApiMock(), getWeatherApiMock())
        repository = component.getDatastoreRepository()
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