package com.softcat.weatherapp

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.softcat.data.mapper.toDbModel
import com.softcat.data.network.api.DocsApiService
import com.softcat.database.facade.DatabaseFacade
import com.softcat.database.model.WeatherTypeDbModel
import com.softcat.domain.interfaces.DatabaseLoaderRepository
import com.softcat.weatherapp.MockCreator.getDataStoreMock
import com.softcat.weatherapp.MockCreator.getDatabaseMock
import com.softcat.weatherapp.MockCreator.getDocsApiMock
import com.softcat.weatherapp.MockCreator.getWeatherApiMock
import com.softcat.weatherapp.TestDataCreator.getCityList
import com.softcat.weatherapp.TestDataCreator.getCityQuery
import com.softcat.weatherapp.TestDataCreator.getTestConditions
import com.softcat.weatherapp.TestDataCreator.getTestForecast
import com.softcat.weatherapp.di.components.DaggerUnitTestsComponent
import com.softcat.weatherapp.di.components.UnitTestsComponent
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers.anyList
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.Mockito.anyString
import org.mockito.Mockito.times
import org.mockito.Mockito.`when`
import org.mockito.kotlin.capture
import org.mockito.kotlin.verify
import kotlin.random.Random

@RunWith(AndroidJUnit4::class)
class DatabaseLoaderRepositoryTest {

    private val context = InstrumentationRegistry
        .getInstrumentation().targetContext.applicationContext

    private lateinit var database: DatabaseFacade
    private lateinit var docsApi: DocsApiService
    private lateinit var component: UnitTestsComponent
    private lateinit var repository: DatabaseLoaderRepository

    @Before
    fun initRepository() {
        database = getDatabaseMock()
        docsApi = getDocsApiMock()
        component = DaggerUnitTestsComponent.factory()
            .create(context, getDataStoreMock(), database, getDocsApiMock(), getWeatherApiMock())
        repository = component.getDatabaseLoaderRepository()
    }

    @Test
    fun searchCities() = runBlocking {
        val cities = getCityList().mapIndexed { i, elem -> elem.toDbModel(i) }
        `when`(database.searchCity(anyString())).thenReturn(Result.success(cities))
        val query = getCityQuery()

        val result = repository.searchCities(query)

        verify(database.searchCity(query), times(1))
        assert(result.isSuccess)
        assert(result.getOrThrow() == cities)
    }

    @Test
    fun searchCitiesEmptyResult() = runBlocking {
        `when`(database.searchCity(anyString())).thenReturn(Result.success(emptyList()))
        val query = getCityQuery()

        val result = repository.searchCities(query)

        verify(database.searchCity(query), times(1))
        assert(result.isSuccess)
        assert(result.getOrThrow().isEmpty())
    }

    @Test
    fun getHourlyWeather() = runBlocking {
        val cityId = Random.nextInt(100, 1000)
        val day = Random.nextInt(0, 360)

        val result = repository.tryGetHourlyWeather(cityId, day)

        verify(database.getCurrentWeather(cityId, anyLong()), times(1))
        assert(result.isSuccess)
        assert(result.getOrThrow().isEmpty())
    }

    @Test
    fun getUpcomingWeather() = runBlocking {
        val cityId = Random.nextInt(100, 1000)
        val dayCount = Random.nextInt(1, 5)

        val result = repository.tryLoadUpcomingDaysWeather(cityId, dayCount)

        verify(database.getDaysWeather(cityId, anyLong(), anyLong()), times(1))
        assert(result.isSuccess)
        assert(result.getOrThrow().isEmpty())
    }

    @Test
    fun updateForecast() = runBlocking {
        val cityId = Random.nextInt(100, 1000)
        val forecast = getTestForecast()

        repository.updateForecastData(cityId, forecast)

        forecast.upcoming?.forEach { dayWeather ->
            verify(database.saveWeather(dayWeather.toDbModel(cityId)), times(1))
        }
        forecast.hourly.forEach { weatherList ->
            weatherList?.forEach {
                verify(database.saveCurrentWeather(it.toDbModel(cityId)), times(1))
            }
        }
    }

    @Test
    fun updateHourlyWeather() = runBlocking {
        val cityId = Random.nextInt(100, 1000)
        val hourlyWeather = getTestForecast().hourly.first()!!

        repository.updateHourlyWeatherData(cityId, hourlyWeather)

        hourlyWeather.forEach {
            verify(database.saveCurrentWeather(it.toDbModel(cityId)), times(1))
        }
    }

    @Test
    fun updateDayWeather() = runBlocking {
        val cityId = Random.nextInt(100, 1000)
        val weather = getTestForecast().upcoming!!

        repository.updateDayWeatherData(cityId, weather)

        weather.forEach { dayWeather ->
            verify(database.saveWeather(dayWeather.toDbModel(cityId)), times(1))
        }
    }

    @Test
    fun loadWeatherTypesToDatabase() = runBlocking {
        val conditions = getTestConditions()
        `when`(docsApi.loadWeatherConditions()).thenReturn(conditions)
        `when`(database.initWeatherTypes(anyList())).thenAnswer { invocation ->
            val list = invocation.arguments.first() as List<WeatherTypeDbModel>
            assert(list.size == conditions.size)
        }

        val result = repository.loadWeatherTypesToDatabase()

        assert(result.isSuccess)
        verify(database.initWeatherTypes(anyList()), times(1))
    }

    @Test
    fun clearWeatherData() = runBlocking {
        repository.clearWeatherData()

        verify(database.clearWeatherData(), times(1))
    }
}