package com.softcat.weatherapp

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.softcat.data.implementations.SearchRepositoryImpl
import com.softcat.data.mapper.toEntities
import com.softcat.database.model.CityDbModel
import com.softcat.weatherapp.MockCreator.getDatabaseMock
import com.softcat.weatherapp.MockCreator.getWeatherApiMock
import com.softcat.weatherapp.TestDataCreator.getCityDtoList
import com.softcat.weatherapp.TestDataCreator.getCityQuery
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Mockito.anyList
import org.mockito.Mockito.anyString
import org.mockito.Mockito.reset
import org.mockito.Mockito.`when`
import org.mockito.kotlin.times
import org.mockito.kotlin.verify

@RunWith(AndroidJUnit4::class)
class SearchRepositoryTest {

    private val database = getDatabaseMock()
    private val weatherApi = getWeatherApiMock()
    private val repository = SearchRepositoryImpl(weatherApi, database)

    @Before
    fun resetMocks() {
        reset(database)
        reset(weatherApi)
    }

    @Test
    fun search() = runBlocking {
        val cities = getCityDtoList()
        `when`(weatherApi.searchCity(anyString())).thenReturn(cities)
        val cityCaptor = ArgumentCaptor.forClass(CityDbModel::class.java)
        val result = repository.search(getCityQuery())

        verify(database, times(cities.size))
            .saveCity(cityCaptor.capture())
        verify(database, times(1))
            .updateCountries(anyList())
        assert(result == cities.toEntities())
        assert(cityCaptor.allValues.map { it.name }.toSet() == cities.map { it.name }.toSet())
    }
}