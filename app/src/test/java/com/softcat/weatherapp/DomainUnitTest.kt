package com.softcat.weatherapp

import android.content.Context
import com.softcat.weatherapp.domain.entity.City
import com.softcat.weatherapp.domain.entity.WeatherParameters
import com.softcat.weatherapp.domain.interfaces.CalendarRepository
import com.softcat.weatherapp.domain.interfaces.DatastoreRepository
import com.softcat.weatherapp.domain.interfaces.FavouriteRepository
import com.softcat.weatherapp.domain.interfaces.LocationRepository
import com.softcat.weatherapp.domain.interfaces.SearchRepository
import com.softcat.weatherapp.domain.interfaces.WeatherRepository
import com.softcat.weatherapp.domain.useCases.AddToFavouriteUseCase
import com.softcat.weatherapp.domain.useCases.GetCurrentCityNameUseCase
import com.softcat.weatherapp.domain.useCases.GetCurrentWeatherUseCase
import com.softcat.weatherapp.domain.useCases.GetForecastUseCase
import com.softcat.weatherapp.domain.useCases.GetHighlightedDaysUseCase
import com.softcat.weatherapp.domain.useCases.GetLastCityFromDatastoreUseCase
import com.softcat.weatherapp.domain.useCases.GetTodayForecastUseCase
import com.softcat.weatherapp.domain.useCases.ObserveIsFavouriteUseCase
import com.softcat.weatherapp.domain.useCases.RemoveFromFavouriteUseCase
import com.softcat.weatherapp.domain.useCases.ResetHighlightedDaysUseCase
import com.softcat.weatherapp.domain.useCases.SaveToDatastoreUseCase
import com.softcat.weatherapp.domain.useCases.SearchCityUseCase
import com.softcat.weatherapp.domain.useCases.SelectYearDaysUseCase
import kotlinx.coroutines.runBlocking
import org.junit.Test

import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import kotlin.random.Random

class DomainUnitTest {

    private val calendarRepository = mock(CalendarRepository::class.java)
    private val datastoreRepository = mock(DatastoreRepository::class.java)
    private val favouriteRepository = mock(FavouriteRepository::class.java)
    private val locationRepository = mock(LocationRepository::class.java)
    private val searchRepository = mock(SearchRepository::class.java)
    private val weatherRepository = mock(WeatherRepository::class.java)
    private val context = mock(Context::class.java)

    @Test
    fun addToFavouriteUseCaseTest(): Unit = runBlocking {
        val city = City(
            id = Random.nextInt(1000, 10000),
            name = "Moscow",
            country = "Russia"
        )
        val addToFavouriteUseCase = AddToFavouriteUseCase(favouriteRepository)
        addToFavouriteUseCase(city)
        verify(favouriteRepository, times(1))
            .addToFavourite(city)
    }

    @Test
    fun getCurrentCityNameUseCaseTest(): Unit = runBlocking {
        val getCurrentCityNameUseCase = GetCurrentCityNameUseCase(locationRepository)
        val callback: (String) -> Unit = {}
        getCurrentCityNameUseCase(context, callback)
        verify(locationRepository, times(1))
            .getCurrentCity(context, callback)
    }

    @Test
    fun getCurrentWeatherUseCaseTest(): Unit = runBlocking {
        val getCurrentWeatherUseCase = GetCurrentWeatherUseCase(weatherRepository)
        val cityId = Random.nextInt(1000, 10000)
        getCurrentWeatherUseCase(cityId)
        verify(weatherRepository, times(1))
            .getWeather(cityId)
    }

    @Test
    fun getForecastUseCaseTest(): Unit = runBlocking {
        val getForecastUseCase = GetForecastUseCase(weatherRepository)
        val cityId = Random.nextInt(1000, 10000)
        getForecastUseCase(cityId)
        verify(weatherRepository, times(1))
            .getForecast(cityId)
    }

    @Test
    fun getHighlightedDaysUseCaseTest(): Unit = runBlocking {
        val getHighlightedDaysUseCase = GetHighlightedDaysUseCase(calendarRepository)
        getHighlightedDaysUseCase()
        verify(calendarRepository, times(1))
            .getHighlightedDays()
    }

    @Test
    fun getLastCityFromDatastoreUseCaseTest(): Unit = runBlocking {
        val getLastCityFromDatastoreUseCase = GetLastCityFromDatastoreUseCase(datastoreRepository)
        getLastCityFromDatastoreUseCase()
        verify(datastoreRepository, times(1))
            .getLastCityFromDatastore()
    }

    @Test
    fun getTodayForecastUseCaseTest(): Unit = runBlocking {
        val getTodayForecastUseCase = GetTodayForecastUseCase(weatherRepository)
        val cityId = Random.nextInt(1000, 10000)
        getTodayForecastUseCase(cityId)
        verify(weatherRepository, times(1))
            .getTodayLocalForecast(cityId)
    }

    @Test
    fun observeIsFavouriteUseCaseTest(): Unit = runBlocking {
        val observeIsFavouriteUseCase = ObserveIsFavouriteUseCase(favouriteRepository)
        val cityId = Random.nextInt(1000, 10000)
        observeIsFavouriteUseCase(cityId)
        verify(favouriteRepository, times(1))
            .observeIsFavourite(cityId)
    }

    @Test
    fun removeFromFavouriteUseCaseTest(): Unit = runBlocking {
        val removeFromFavouriteUseCase = RemoveFromFavouriteUseCase(favouriteRepository)
        val cityId = Random.nextInt(1000, 10000)
        removeFromFavouriteUseCase(cityId)
        verify(favouriteRepository, times(1))
            .removeFromFavourite(cityId)
    }

    @Test
    fun resetHighlightedDaysUseCaseTest(): Unit = runBlocking {
        val resetHighlightedDaysUseCase = ResetHighlightedDaysUseCase(calendarRepository)
        resetHighlightedDaysUseCase()
        verify(calendarRepository, times(1))
            .reset()
    }

    @Test
    fun saveToDatastoreUseCaseTest(): Unit = runBlocking {
        val saveToDatastoreUseCase = SaveToDatastoreUseCase(datastoreRepository)
        val value = "some text"
        saveToDatastoreUseCase(value)
        verify(datastoreRepository, times(1))
            .saveCityToDatastore(value)
    }

    @Test
    fun searchCityUseCaseTest(): Unit = runBlocking {
        val searchCityUseCase = SearchCityUseCase(searchRepository)
        val value = "some text"
        searchCityUseCase(value)
        verify(searchRepository, times(1))
            .search(value)
    }

    @Test
    fun selectYearDaysUseCaseTest(): Unit = runBlocking {
        val selectYearDaysUseCase = SelectYearDaysUseCase(calendarRepository)
        val params = WeatherParameters()
        val city = City(
            id = Random.nextInt(1000, 10000),
            name = "Moscow",
            country = "Russia"
        )
        val year = 2025
        selectYearDaysUseCase(params, city, year)
        verify(calendarRepository, times(1))
            .selectYearDays(params, city, year)
    }
}