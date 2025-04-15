package com.softcat.weatherapp

import com.softcat.weatherapp.data.implementations.SearchRepositoryImpl
import com.softcat.weatherapp.data.network.dto.CityDto
import com.softcat.weatherapp.domain.entity.City
import com.softcat.weatherapp.domain.interfaces.SearchRepository
import com.softcat.weatherapp.objectMocks.apiServiceMock
import com.softcat.weatherapp.objectMocks.searchResultDto
import com.softcat.weatherapp.objectMocks.setupApiServiceMock
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.times
import org.mockito.Mockito.verify

class SearchRepositoryTest {
    @Before
    fun setupMocks() {
        setupApiServiceMock()
    }

    private fun checkCityConversion(result: List<City>, dtoList: List<CityDto>) {
        assert(result.size == dtoList.size)
        for (i in 0 until result.size) {
            assert(result[i].id == dtoList[i].id)
            assert(result[i].name == dtoList[i].name)
            assert(result[i].country == dtoList[i].country)
        }
    }

    @Test
    fun searchCityTest() = runBlocking {
        val repository: SearchRepository = SearchRepositoryImpl(
            apiService = apiServiceMock
        )
        val query = "Moscow"
        val result = repository.search(query)
        verify(apiServiceMock, times(1))
            .searchCity(query)
        checkCityConversion(result, searchResultDto)
    }
}