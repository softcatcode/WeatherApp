package com.softcat.weatherapp

import com.google.gson.Gson
import com.softcat.domain.entity.City
import com.softcat.domain.entity.WeatherParameters
import com.softcat.domain.entity.weatherTypeOf
import com.softcat.domain.interfaces.CalendarRepository
import com.softcat.weatherapp.data.implementations.CalendarRepositoryImpl
import com.softcat.weatherapp.data.network.api.ApiService
import com.softcat.weatherapp.data.network.dto.WeatherForecastDto
import com.softcat.weatherapp.jsonMocks.weatherForecastJson
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.anyInt
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.kotlin.whenever
import java.util.Calendar
import kotlin.random.Random

class CalendarRepositoryTest {

    private val apiServiceMock = mock(ApiService::class.java)
    private val gson = Gson()
    private val weatherForecastDto = gson.fromJson(weatherForecastJson, WeatherForecastDto::class.java)

    @Before
    fun setup(): Unit = runBlocking {
        whenever(
            apiServiceMock.loadForecast(anyString(), anyInt())
        ).thenReturn(weatherForecastDto)
        whenever(
            apiServiceMock.loadWeatherHistory(anyString(), anyString(), anyString())
        ).thenReturn(weatherForecastDto)
    }

    @Test
    fun emptyResultTest(): Unit = runBlocking {
        val repository: CalendarRepository = CalendarRepositoryImpl(apiServiceMock)
        val params = WeatherParameters(
            windSpeedMin = 200f,
            windSpeedMax = 200f
        )
        val year = 2003
        val city = City(
            id = Random.nextInt(100, 200),
            name = "Moscow",
            country = "Russia"
        )
        val result = repository.selectYearDays(params, city, year)
        result.onSuccess { days ->
            verify(apiServiceMock, times(0))
                .loadForecast(anyString(), anyInt())
            verify(apiServiceMock, times(1))
                .loadWeatherHistory("id:${city.id}", "$year-01-01", "$year-12-31")
            assertTrue(days.size == 12)
            for (set in days)
                assertTrue(set.isEmpty())
        }.onFailure { throwable ->
            throw throwable
        }
    }

    @Test
    fun oneDayResultTest(): Unit = runBlocking {
        val repository: CalendarRepository = CalendarRepositoryImpl(apiServiceMock)
        val params = WeatherParameters(
            temperatureMin = 0f,
            temperatureMax = 5f
        )
        val year = Calendar.getInstance().get(Calendar.YEAR)
        val city = City(
            id = Random.nextInt(100, 200),
            name = "Moscow",
            country = "Russia"
        )

        repository.selectYearDays(params, city, year).onSuccess { result ->
            verify(apiServiceMock, times(1))
                .loadForecast("id:${city.id}", 10)
            verify(apiServiceMock, times(0))
                .loadWeatherHistory(anyString(), anyString(), anyString())
            assert(result.size == 12)
            for (i in 0 until 12) {
                if (i == 3) {
                    assert(result[i].size == 1)
                    assert(result[i].first() == 5)
                }
                else
                    assertTrue(result[i].isEmpty())
            }
        }.onFailure { throwable ->
            throw throwable
        }
    }

    @Test
    fun selectWeatherOfTypeTest(): Unit = runBlocking {
        val repository: CalendarRepository = CalendarRepositoryImpl(apiServiceMock)
        val code = weatherForecastDto.forecastDto.days[1].weather.condition.code
        val params = WeatherParameters(
            weatherType = weatherTypeOf(code)
        )
        val year = Calendar.getInstance().get(Calendar.YEAR)
        val city = City(
            id = Random.nextInt(100, 200),
            name = "Moscow",
            country = "Russia"
        )
        repository.selectYearDays(params, city, year).onSuccess { result ->
            verify(apiServiceMock, times(1))
                .loadForecast("id:${city.id}", 10)
            verify(apiServiceMock, times(0))
                .loadWeatherHistory(anyString(), anyString(), anyString())
            assert(result.size == 12)
            for (i in 0 until 12) {
                if (i == 3)
                    assert(result[i] == setOf(5, 6))
                else
                    assertTrue(result[i].isEmpty())
            }
        }.onFailure { throwable ->
            throw throwable
        }
    }

    @Test
    fun twoDaysResultTest(): Unit = runBlocking {
        val repository: CalendarRepository = CalendarRepositoryImpl(apiServiceMock)
        val params = WeatherParameters(
            temperatureMin = -5f,
            temperatureMax = 5f
        )
        val year = Calendar.getInstance().get(Calendar.YEAR)
        val city = City(
            id = Random.nextInt(100, 200),
            name = "Moscow",
            country = "Russia"
        )
        repository.selectYearDays(params, city, year).onSuccess { result ->
            verify(apiServiceMock, times(1))
                .loadForecast("id:${city.id}", 10)
            verify(apiServiceMock, times(0))
                .loadWeatherHistory(anyString(), anyString(), anyString())
            assert(result.size == 12)
            for (i in 0 until 12) {
                if (i == 3)
                    assert(result[i] == setOf(5, 6))
                else
                    assertTrue(result[i].isEmpty())
            }
        }.onFailure { throwable ->
            throw throwable
        }
    }
}