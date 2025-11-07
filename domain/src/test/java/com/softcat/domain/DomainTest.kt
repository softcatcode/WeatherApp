package com.softcat.domain

import android.content.Context
import com.softcat.domain.TestDataCreator.getRandomEmail
import com.softcat.domain.TestDataCreator.getRandomLogin
import com.softcat.domain.TestDataCreator.getRandomPassword
import com.softcat.domain.TestDataCreator.getTestUser
import com.softcat.domain.entity.City
import com.softcat.domain.entity.WeatherParameters
import com.softcat.domain.entity.WeatherType
import com.softcat.domain.entity.toIconResId
import com.softcat.domain.entity.toTitleResId
import com.softcat.domain.entity.weatherTypeOf
import com.softcat.domain.interfaces.AuthorizationRepository
import com.softcat.domain.interfaces.CalendarRepository
import com.softcat.domain.interfaces.DatabaseLoaderRepository
import com.softcat.domain.interfaces.DatastoreRepository
import com.softcat.domain.interfaces.FavouriteRepository
import com.softcat.domain.interfaces.LocationRepository
import com.softcat.domain.interfaces.SearchRepository
import com.softcat.domain.interfaces.WeatherRepository
import com.softcat.domain.useCases.AddToFavouriteUseCase
import com.softcat.domain.useCases.AuthorizationUseCase
import com.softcat.domain.useCases.ClearWeatherDataUseCase
import com.softcat.domain.useCases.GetCurrentCityNameUseCase
import com.softcat.domain.useCases.GetCurrentWeatherUseCase
import com.softcat.domain.useCases.GetFavouriteCitiesUseCase
import com.softcat.domain.useCases.GetForecastUseCase
import com.softcat.domain.useCases.GetLastCityFromDatastoreUseCase
import com.softcat.domain.useCases.GetTodayForecastUseCase
import com.softcat.domain.useCases.LoadWeatherTypesUseCase
import com.softcat.domain.useCases.ObserveIsFavouriteUseCase
import com.softcat.domain.useCases.RemoveFromFavouriteUseCase
import com.softcat.domain.useCases.SaveToDatastoreUseCase
import com.softcat.domain.useCases.SearchCityUseCase
import com.softcat.domain.useCases.SelectYearDaysUseCase
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.mock
import org.mockito.Mockito.reset
import org.mockito.Mockito.times
import org.mockito.Mockito.`when`
import org.mockito.kotlin.verify
import kotlin.random.Random


class DomainTest {

    private val context = mock(Context::class.java)

    private val calendarRepository = mock(CalendarRepository::class.java)
    private val datastoreRepository = mock(DatastoreRepository::class.java)
    private val favouriteRepository = mock(FavouriteRepository::class.java)
    private val locationRepository = mock(LocationRepository::class.java)
    private val searchRepository = mock(SearchRepository::class.java)
    private val weatherRepository = mock(WeatherRepository::class.java)
    private val databaseRepository = mock(DatabaseLoaderRepository::class.java)
    private val authRepository = mock(AuthorizationRepository::class.java)

    private fun getRandomUserId() = "user-${Random.nextInt(100, 500)}"

    private fun getRandomCity() = City(
        id = Random.nextInt(1000, 10000),
        name = listOf("Moscow", "Kazan", "S. Petersburg", "Rostov", "Novgorod")[Random.nextInt(0, 4)],
        country = "Russia",
        latitude = Random.nextInt(-90, 90).toFloat() / 100f,
        longitude = Random.nextInt(-90, 90).toFloat() / 100f
    )

    @Before
    fun resetMocks() {
        reset(calendarRepository)
        reset(datastoreRepository)
        reset(favouriteRepository)
        reset(locationRepository)
        reset(searchRepository)
        reset(weatherRepository)
        reset(databaseRepository)
    }

    @Test
    fun addToFavourite(): Unit = runBlocking {
        val userId = getRandomUserId()
        val city = getRandomCity()
        val addToFavouriteUseCase = AddToFavouriteUseCase(favouriteRepository)

        addToFavouriteUseCase(userId, city)

        verify(favouriteRepository, times(1))
            .addToFavourite(userId, city)
    }

    @Test
    fun getFavourite(): Unit = runBlocking {
        val userId = getRandomUserId()
        val getFavouriteUseCase = GetFavouriteCitiesUseCase(favouriteRepository)

        getFavouriteUseCase(userId)

        verify(favouriteRepository, times(1))
            .getFavouriteCities(userId)
    }

    @Test
    fun clearWeather(): Unit = runBlocking {
        val clearUseCase = ClearWeatherDataUseCase(databaseRepository)

        clearUseCase()

        verify(databaseRepository, times(1))
            .clearWeatherData()
    }

    @Test
    fun getCurrentCityName(): Unit = runBlocking {
        val getCurrentCityNameUseCase = GetCurrentCityNameUseCase(locationRepository)
        val callback: (String) -> Unit = {}

        getCurrentCityNameUseCase(context, callback)

        verify(locationRepository, times(1))
            .getCurrentCity(context, callback)
    }

    @Test
    fun getCurrentWeather(): Unit = runBlocking {
        val getCurrentWeatherUseCase = GetCurrentWeatherUseCase(weatherRepository)
        val cityId = Random.nextInt(1000, 10000)

        getCurrentWeatherUseCase(cityId)

        verify(weatherRepository, times(1))
            .getWeather(cityId)
    }

    @Test
    fun getForecast(): Unit = runBlocking {
        val getForecastUseCase = GetForecastUseCase(weatherRepository, databaseRepository)
        val cityId = Random.nextInt(1000, 10000)

        getForecastUseCase(cityId)

        verify(weatherRepository, times(1))
            .getForecast(cityId)
        verify(databaseRepository, times(0))
            .tryGetHourlyWeather(anyInt(), anyInt())
        verify(databaseRepository, times(0))
            .tryLoadUpcomingDaysWeather(anyInt(), anyInt())
    }

    @Test
    fun getHighlightedDays(): Unit = runBlocking {
        val selectYearDaysUseCase = SelectYearDaysUseCase(calendarRepository)
        val params = WeatherParameters()
        val city = getRandomCity()
        val year = Random.nextInt(2000, 2025)
        
        selectYearDaysUseCase(params, city, year)
        
        verify(calendarRepository, times(1))
            .selectYearDays(params, city, year)
    }

    @Test
    fun getLastCityFromDatastore(): Unit = runBlocking {
        val getLastCityFromDatastoreUseCase = GetLastCityFromDatastoreUseCase(datastoreRepository)
        
        getLastCityFromDatastoreUseCase()
        
        verify(datastoreRepository, times(1))
            .getLastCityFromDatastore()
    }

    @Test
    fun getTodayForecast(): Unit = runBlocking {
        val getTodayForecastUseCase = GetTodayForecastUseCase(weatherRepository, databaseRepository)
        val cityId = Random.nextInt(1000, 10000)

        getTodayForecastUseCase(cityId)

        verify(weatherRepository, times(1))
            .getTodayLocalForecast(cityId)
        verify(databaseRepository, times(0))
            .tryGetHourlyWeather(anyInt(), anyInt())
    }

    @Test
    fun observeIsFavourite(): Unit = runBlocking {
        val observeIsFavouriteUseCase = ObserveIsFavouriteUseCase(favouriteRepository)
        val cityId = Random.nextInt(1000, 10000)
        val userId = getRandomUserId()

        observeIsFavouriteUseCase(userId, cityId)

        verify(favouriteRepository, times(1))
            .observeIsFavourite(userId, cityId)
    }

    @Test
    fun removeFromFavourite(): Unit = runBlocking {
        val removeFromFavouriteUseCase = RemoveFromFavouriteUseCase(favouriteRepository)
        val cityId = Random.nextInt(1000, 10000)
        val userId = getRandomUserId()

        removeFromFavouriteUseCase(userId, cityId)

        verify(favouriteRepository, times(1))
            .removeFromFavourite(userId, cityId)
    }

    @Test
    fun saveToDatastore(): Unit = runBlocking {
        val saveToDatastoreUseCase = SaveToDatastoreUseCase(datastoreRepository)
        val value = "number-${Random.nextInt(100, 10000)}"

        saveToDatastoreUseCase(value)

        verify(datastoreRepository, times(1))
            .saveCityToDatastore(value)
    }

    @Test
    fun searchCity(): Unit = runBlocking {
        val cityList = List(5) { getRandomCity() }
        `when`(searchRepository.search(anyString())).thenReturn(cityList)
        val searchCityUseCase = SearchCityUseCase(searchRepository, databaseRepository)
        val query = "number-${Random.nextInt(100, 10000)}"

        val result = searchCityUseCase(query)

        assert(result.isSuccess)
        assert(result.getOrNull() == cityList)
        verify(searchRepository, times(1))
            .search(query)
        verify(databaseRepository, times(0))
            .searchCities(anyString())
    }

    @Test
    fun selectYearDays(): Unit = runBlocking {
        val selectYearDaysUseCase = SelectYearDaysUseCase(calendarRepository)
        val params = WeatherParameters()
        val city = getRandomCity()
        val year = Random.nextInt(2000, 2025)

        selectYearDaysUseCase(params, city, year)

        verify(calendarRepository, times(1))
            .selectYearDays(params, city, year)
    }

    @Test
    fun signIn(): Unit = runBlocking {
        val authUseCase = AuthorizationUseCase(authRepository, datastoreRepository)
        val login = getRandomLogin()
        val email = getRandomEmail()
        val password = getRandomPassword()

        authUseCase.register(login, email, password)

        verify(authRepository, times(1))
            .register(login, email, password)
    }

    @Test
    fun enter(): Unit = runBlocking {
        val authUseCase = AuthorizationUseCase(authRepository, datastoreRepository)
        val login = getRandomLogin()
        val password = getRandomPassword()

        authUseCase.logIn(login, password)

        verify(authRepository, times(1))
            .enter(login, password)
    }

    @Test
    fun rememberUser(): Unit = runBlocking {
        val authUseCase = AuthorizationUseCase(authRepository, datastoreRepository)
        val user = getTestUser()

        authUseCase.rememberUser(user)

        verify(datastoreRepository, times(1))
            .saveLastUser(user)
    }

    @Test
    fun getLastUser(): Unit = runBlocking {
        val authUseCase = AuthorizationUseCase(authRepository, datastoreRepository)

        authUseCase.getLastUser()

        verify(datastoreRepository, times(1))
            .getLastUser()
    }

    @Test
    fun loadWeatherTypes(): Unit = runBlocking {
        val loadWeatherTypesUseCase = LoadWeatherTypesUseCase(databaseRepository)

        loadWeatherTypesUseCase()

        verify(databaseRepository, times(1))
            .loadWeatherTypesToDatabase()
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

        params = params.updateHumidity(10..20)
        params = params.updateWindSpeed(10..20)
        params = params.updateSnowVolume(10..20)
        params = params.updatePrecipitations(10..20)
        params = params.updateMinTemperature("10")
        params = params.updateMaxTemperature("20")

        var a = params.updateMaxTemperature("-0..1")
        a = a.updateMinTemperature("2-e")
        a = a.updateMaxTemperature((WeatherParameters.MAX_TEMPERATURE + 0.1f).toString())
        a = a.updateMinTemperature((WeatherParameters.MIN_TEMPERATURE - 0.1f).toString())
        val b = a.updateMinTemperature("30")
        val c = a.updateMaxTemperature("-1")

        with (params) {
            assert(humidity == 10..20)
            assert(windSpeed == 10..20)
            assert(snowVolume == 10..20)
            assert(precipitations == 10..20)
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