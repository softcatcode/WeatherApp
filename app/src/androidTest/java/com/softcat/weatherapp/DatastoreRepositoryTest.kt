package com.softcat.weatherapp

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.softcat.data.implementations.DatastoreRepositoryImpl
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DatastoreRepositoryTest {

    private val applicationContext = InstrumentationRegistry
        .getInstrumentation().targetContext.applicationContext
    private val application = applicationContext as WeatherApplication

    @Test
    fun useAppContext() = runBlocking {
        val repository = DatastoreRepositoryImpl(application.dataStore)
        val savedCity = "London"
        repository.saveCityToDatastore(savedCity)
        val city1 = repository.getLastCityFromDatastore()
        val city2 = repository.getLastCityFromDatastore()

        assert(city1 == savedCity)
        assert(city2 == savedCity)
    }
}