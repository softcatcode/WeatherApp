package com.softcat.weatherapp

import android.content.Context
import com.softcat.weatherapp.domain.entity.City
import com.softcat.weatherapp.domain.entity.WeatherParameters
import com.softcat.weatherapp.domain.entity.WeatherType
import com.softcat.weatherapp.domain.entity.toIconResId
import com.softcat.weatherapp.domain.entity.toTitleResId
import com.softcat.weatherapp.domain.entity.weatherTypeOf
import com.softcat.weatherapp.domain.interfaces.CalendarRepository
import com.softcat.weatherapp.domain.interfaces.DatastoreRepository
import com.softcat.weatherapp.domain.interfaces.FavouriteRepository
import com.softcat.weatherapp.domain.interfaces.LocationRepository
import com.softcat.weatherapp.domain.interfaces.SearchRepository
import com.softcat.weatherapp.domain.interfaces.WeatherRepository
import com.softcat.weatherapp.domain.useCases.AddToFavouriteUseCase
import com.softcat.weatherapp.domain.useCases.GetCurrentCityNameUseCase
import com.softcat.weatherapp.domain.useCases.GetCurrentWeatherUseCase
import com.softcat.weatherapp.domain.useCases.GetFavouriteCitiesUseCase
import com.softcat.weatherapp.domain.useCases.GetForecastUseCase
import com.softcat.weatherapp.domain.useCases.GetLastCityFromDatastoreUseCase
import com.softcat.weatherapp.domain.useCases.GetTodayForecastUseCase
import com.softcat.weatherapp.domain.useCases.ObserveIsFavouriteUseCase
import com.softcat.weatherapp.domain.useCases.RemoveFromFavouriteUseCase
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

    @Test
    fun getFavouriteCitiesUseCaseTest(): Unit = runBlocking {
        val getFavouriteCitiesUseCase = GetFavouriteCitiesUseCase(favouriteRepository)
        getFavouriteCitiesUseCase()
        verify(favouriteRepository, times(1))
            .getFavouriteCities()
    }
    
    @Test
    fun weatherParametersConstructorTest() = with (WeatherParameters()) {
        assert(weatherType == WeatherType.Any)

        assert(humidityMin == WeatherParameters.MIN_HUMIDITY)
        assert(precipitationsMin == WeatherParameters.MIN_PRECIPITATIONS)
        assert(snowVolumeMin == WeatherParameters.MIN_SNOW_VOLUME)
        assert(windSpeedMin == WeatherParameters.MIN_WIND_SPEED)
        assert(temperatureMin == WeatherParameters.MIN_TEMPERATURE)

        assert(humidityMax == WeatherParameters.MAX_HUMIDITY)
        assert(precipitationsMax == WeatherParameters.MAX_PRECIPITATIONS)
        assert(snowVolumeMax == WeatherParameters.MAX_SNOW_VOLUME)
        assert(windSpeedMax == WeatherParameters.MAX_WIND_SPEED)
        assert(temperatureMax == WeatherParameters.MAX_TEMPERATURE)

        assert(humidity == humidityMin..humidityMax)
        assert(precipitations == precipitationsMin..precipitationsMax)
        assert(snowVolume == snowVolumeMin..snowVolumeMax)
        assert(windSpeed == windSpeedMin..windSpeedMax)
        assert(temperature == temperatureMin..temperatureMax)
    }

    @Test
    fun weatherParametersMethodsTest() {
        var params = WeatherParameters()

        params = params.updateHumidity(10f..20f)
        params = params.updateWindSpeed(10f..20f)
        params = params.updateSnowVolume(10f..20f)
        params = params.updatePrecipitations(10f..20f)
        params = params.updateMinTemperature("10")
        params = params.updateMaxTemperature("20")

        var a = params.updateMaxTemperature("-0..1")
        a = a.updateMinTemperature("2-e")
        a = a.updateMaxTemperature((WeatherParameters.MAX_TEMPERATURE + 0.1f).toString())
        a = a.updateMinTemperature((WeatherParameters.MIN_TEMPERATURE - 0.1f).toString())
        val b = a.updateMinTemperature("30")
        val c = a.updateMaxTemperature("-1")

        with (params) {
            assert(humidity == 10f..20f)
            assert(windSpeed == 10f..20f)
            assert(snowVolume == 10f..20f)
            assert(precipitations == 10f..20f)
            assert(temperature == 10f..20f)
            assert(a.temperature == 10f..20f)
            assert(b.temperature == 10f..20f)
            assert(c.temperature == 10f..20f)
        }
    }

    @Test
    fun weatherTypeToResourceTest() {
        assert(WeatherType.Thunderstorm.toIconResId() == R.drawable.thunderstorm)
        assert(WeatherType.Drizzle.toIconResId() == R.drawable.drizzle)
        assert(WeatherType.Rain.toIconResId() == R.drawable.rain)
        assert(WeatherType.ShowerRain.toIconResId() == R.drawable.shower)
        assert(WeatherType.Mist.toIconResId() == R.drawable.mist)
        assert(WeatherType.Sun.toIconResId() == R.drawable.sun)
        assert(WeatherType.Clouds.toIconResId() == R.drawable.clouds)
        assert(WeatherType.HeavyClouds.toIconResId() == R.drawable.heavy_clouds)
        assert(WeatherType.Snow.toIconResId() == R.drawable.snow)
        assert(WeatherType.Any.toIconResId() == R.drawable.any_weather)
    }

    @Test
    fun weatherTypeToStringResourceTest() {
        assert(WeatherType.Thunderstorm.toTitleResId() == R.string.thunderstorm)
        assert(WeatherType.Drizzle.toTitleResId() == R.string.drizzle)
        assert(WeatherType.Rain.toTitleResId() == R.string.rain)
        assert(WeatherType.ShowerRain.toTitleResId() == R.string.shower)
        assert(WeatherType.Mist.toTitleResId() == R.string.mist)
        assert(WeatherType.Sun.toTitleResId() == R.string.sun)
        assert(WeatherType.Clouds.toTitleResId() == R.string.clouds)
        assert(WeatherType.HeavyClouds.toTitleResId() == R.string.heavy_clouds)
        assert(WeatherType.Snow.toTitleResId() == R.string.snow)
        assert(WeatherType.Any.toTitleResId() == R.string.any)
    }

    @Test
    fun weatherTypeDeterminationTest() {
        assert(weatherTypeOf(-1) == WeatherType.Any)
        assert(weatherTypeOf(1000) == WeatherType.Sun)

        assert(weatherTypeOf(1003) == WeatherType.Clouds)
        assert(weatherTypeOf(1006) == WeatherType.Clouds)

        assert(weatherTypeOf(1009) == WeatherType.HeavyClouds)
        assert(weatherTypeOf(1030) == WeatherType.Mist)

        assert(weatherTypeOf(1273) == WeatherType.Thunderstorm)
        assert(weatherTypeOf(1276) == WeatherType.Thunderstorm)
        assert(weatherTypeOf(1279) == WeatherType.Thunderstorm)
        assert(weatherTypeOf(1282) == WeatherType.Thunderstorm)

        assert(weatherTypeOf(1150) == WeatherType.Drizzle)
        assert(weatherTypeOf(1153) == WeatherType.Drizzle)
        assert(weatherTypeOf(1168) == WeatherType.Drizzle)
        assert(weatherTypeOf(1171) == WeatherType.Drizzle)

        assert(weatherTypeOf(1180) == WeatherType.Rain)
        assert(weatherTypeOf(1183) == WeatherType.Rain)
        assert(weatherTypeOf(1186) == WeatherType.Rain)
        assert(weatherTypeOf(1189) == WeatherType.Rain)
        assert(weatherTypeOf(1198) == WeatherType.Rain)
        assert(weatherTypeOf(1201) == WeatherType.Rain)
        assert(weatherTypeOf(1063) == WeatherType.Rain)

        assert(weatherTypeOf(1192) == WeatherType.ShowerRain)
        assert(weatherTypeOf(1195) == WeatherType.ShowerRain)

        assert(weatherTypeOf(1210) == WeatherType.Snow)
        assert(weatherTypeOf(1213) == WeatherType.Snow)
        assert(weatherTypeOf(1216) == WeatherType.Snow)
        assert(weatherTypeOf(1222) == WeatherType.Snow)
        assert(weatherTypeOf(1225) == WeatherType.Snow)
    }
}