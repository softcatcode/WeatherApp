package com.softcat.weatherapp

import android.icu.util.Calendar
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.softcat.data.mapper.toDbModel
import com.softcat.data.mapper.toIconUrl
import com.softcat.database.model.CityDbModel
import com.softcat.database.model.CountryDbModel
import com.softcat.database.model.CurrentWeatherDbModel
import com.softcat.database.model.UserDbModel
import com.softcat.database.model.WeatherDbModel
import com.softcat.database.model.WeatherTypeDbModel
import com.softcat.domain.entity.City
import com.softcat.domain.entity.User
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import java.net.URL
import kotlin.math.abs

@RunWith(AndroidJUnit4::class)
class InsertQueryTest {
    private val context = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext
    private val component = DaggerUnitTestsComponent.factory().create(context)
    private val db = component.getDatabase()
    private val regionManager = component.getRegionManager()
    private val weatherManager = component.getWeatherManager()

    private fun cmp(a: CountryDbModel, b: CountryDbModel) {
        assert(a.name == b.name)
    }

    private fun cmp(a: CityDbModel, b: CityDbModel) {
        assert(a.id == b.id)
        assert(a.name == b.name)
        assert(abs(a.latitude - b.latitude) < 1e-6)
        assert(abs(a.longitude - b.longitude) < 1e-6)
        assert(a.countryId == b.countryId)
    }

    private fun cmp(a: WeatherTypeDbModel, b: WeatherTypeDbModel) {
        assert(a.code == b.code)
        assert(a.url == b.url)
        assert(a.iconBytes.contentEquals(b.iconBytes))
        assert(a.dayDescription == b.dayDescription)
        assert(a.nightDescription == b.nightDescription)
    }

    private fun cmp(a: UserDbModel, b: UserDbModel) {
        assert(a.name == b.name)
        assert(a.email == b.email)
        assert(a.role == b.role)
        assert(a.password == b.password)
        assert(a.registerTimeEpoch == b.registerTimeEpoch)
        assert(a.id == UserDbModel.UNSPECIFIED_ID || a.id == b.id)
    }

    private fun cmp(a: WeatherDbModel, b: WeatherDbModel) {
        assert(a.id == b.id)
        assert(a.type == b.type)
        assert(a.vision == b.vision)
        assert(a.cityId == b.cityId)
        assert(a.avgTemp == b.avgTemp)
        assert(a.humidity == b.humidity)
        assert(a.moonIllumination == b.moonIllumination)
        assert(a.moonPhase == b.moonPhase)
        assert(a.moonriseTime == b.moonriseTime)
        assert(a.moonsetTime == b.moonsetTime)
        assert(a.sunsetTime == b.sunsetTime)
        assert(a.sunriseTime == b.sunriseTime)
        assert(a.precipitations == b.precipitations)
        assert(a.snowVolume == b.snowVolume)
        assert(a.rainChance == b.rainChance)
        assert(a.windSpeed == b.windSpeed)
        assert(a.timeEpoch == b.timeEpoch)
    }

    private fun cmp(a: CurrentWeatherDbModel, b: CurrentWeatherDbModel) {
        assert(a.id == b.id)
        assert(a.type == b.type)
        assert(a.vision == b.vision)
        assert(a.cityId == b.cityId)
        assert(a.tempC == b.tempC)
        assert(a.humidity == b.humidity)
        assert(a.cloud == b.cloud)
        assert(a.isDay == b.isDay)
        assert(a.timeEpoch == b.timeEpoch)
        assert(a.snow == b.snow)
        assert(a.precipitations == b.precipitations)
        assert(a.feelsLike == b.feelsLike)
        assert(a.windSpeed == b.windSpeed)
    }

    @Test
    fun saveTowDifferentCountries() = runBlocking {
        val countryModels = listOf(
            CountryDbModel(
                id = CountryDbModel.UNSPECIFIED_ID,
                name = "Russia"
            ),
            CountryDbModel(
                id = CountryDbModel.UNSPECIFIED_ID,
                name = "England"
            ),
        )

        countryModels.forEach {
            db.saveCountry(it)
        }
        val savedCountries = db.getCountries().getOrThrow()

        assert(savedCountries.size == countryModels.size)
        for (i in countryModels.indices) {
            cmp(countryModels[i], savedCountries[i])
        }
    }

    @Test
    fun saveTwoSameCountries() = runBlocking {
        val countryModels = listOf(
            CountryDbModel(
                id = CountryDbModel.UNSPECIFIED_ID,
                name = "Russia"
            ),
            CountryDbModel(
                id = CountryDbModel.UNSPECIFIED_ID,
                name = "Russia"
            ),
        )

        countryModels.forEach {
            db.saveCountry(it)
        }
        val savedCountries = db.getCountries().getOrThrow()

        assert(savedCountries.size == 1)
        cmp(savedCountries[0], countryModels[0])
    }

    @Test
    fun saveTwoCitiesFromDifferentCountries() = runBlocking {
        val countryModels = listOf(
            CountryDbModel(
                id = CountryDbModel.UNSPECIFIED_ID,
                name = "Russia"
            ),
            CountryDbModel(
                id = CountryDbModel.UNSPECIFIED_ID,
                name = "United Kingdom"
            ),
        )
        val cities = listOf(
            City(
                id = 2145091,
                name = "Moscow",
                country = "Russia",
                latitude = 55.75f,
                longitude = 37.62f
            ),
            City(
                id = 2801268,
                name = "London",
                country = "United Kingdom",
                latitude = 51.52f,
                longitude = -0.11f
            )
        )

        val cityModels = cities.mapIndexed { index, cityModel ->
            val id = db.saveCountry(countryModels[index]).getOrThrow()
            cityModel.toDbModel(id)
        }
        cityModels.forEach { db.saveCity(it) }
        val savedCities = regionManager.getCities(listOf(2145091, 2801268)).getOrThrow()

        assert(savedCities.size == cityModels.size)
        for (i in cityModels.indices) {
            cmp(savedCities[i], cityModels[i])
        }
    }

    @Test
    fun saveTwoCitiesFromSameCountries() = runBlocking {
        val countryModels = listOf(
            CountryDbModel(
                id = CountryDbModel.UNSPECIFIED_ID,
                name = "Russia"
            ),
            CountryDbModel(
                id = CountryDbModel.UNSPECIFIED_ID,
                name = "United Kingdom"
            ),
        )
        val cities = listOf(
            City(
                id = 2145091,
                name = "Moscow",
                country = "Russia",
                latitude = 55.75f,
                longitude = 37.62f
            ),
            City(
                id = 2181569,
                name = "Saint Petersburg",
                country = "Russia",
                latitude = 59.89f,
                longitude = 30.26f
            )
        )

        countryModels.forEach {
            db.saveCountry(it)
        }
        val cityModels = cities.map { cityModel ->
            cityModel.toDbModel(1)
        }
        cityModels.forEach { db.saveCity(it) }
        val savedCities = regionManager.getCities(listOf(2145091, 2181569)).getOrThrow()

        assert(savedCities.size == cityModels.size)
        for (i in cityModels.indices) {
            cmp(savedCities[i], cityModels[i])
        }
    }

    @Test
    fun saveTwoSameCities() = runBlocking {
        val countryModels = listOf(
            CountryDbModel(
                id = CountryDbModel.UNSPECIFIED_ID,
                name = "Russia"
            ),
            CountryDbModel(
                id = CountryDbModel.UNSPECIFIED_ID,
                name = "United Kingdom"
            ),
        )
        val cities = listOf(
            City(
                id = 2145091,
                name = "Moscow",
                country = "Russia",
                latitude = 55.75f,
                longitude = 37.62f
            ),
            City(
                id = 2145091,
                name = "Moscow",
                country = "Russia",
                latitude = 55.75f,
                longitude = 37.62f
            )
        )

        countryModels.forEach {
            db.saveCountry(it)
        }
        val cityModels = cities.map { cityModel ->
            cityModel.toDbModel(1)
        }
        cityModels.forEach { db.saveCity(it) }
        val savedCities = regionManager.getCities(listOf(2145091, 2145091)).getOrThrow()

        assert(savedCities.size == cityModels.size)
        for (i in cityModels.indices) {
            cmp(savedCities[i], cityModels[i])
        }
    }

    @Test
    fun insertWeatherType() = runBlocking {
        val iconUrl = toIconUrl(113)
        val url = URL(iconUrl)
        val iconBytes = url.readBytes()
        val weatherType = WeatherTypeDbModel(
            code = 1000,
            dayDescription = "Sunny",
            nightDescription = "Clear",
            url = iconUrl,
            iconBytes = iconBytes
        )

        weatherManager.updateWeatherTypes(listOf(weatherType))
        val savedTypes = weatherManager.getWeatherTypes(listOf(1000)).getOrThrow()

        assert(savedTypes.size == 1)
        cmp(savedTypes[0], weatherType)
    }

    @Test
    fun insertWeatherTypes() = runBlocking {
        val iconsUrl = listOf(toIconUrl(113), toIconUrl(176))
        val urls = iconsUrl.map { URL(it) }
        val iconsImage = urls.map { it.readBytes() }
        val weatherTypes = listOf(
            WeatherTypeDbModel(
                code = 1000,
                dayDescription = "Sunny",
                nightDescription = "Clear",
                url = iconsUrl[0],
                iconBytes = iconsImage[0]
            ),
            WeatherTypeDbModel(
                code = 1063,
                dayDescription = "Patchy rain possible",
                nightDescription = "Patchy rain possible",
                url = iconsUrl[1],
                iconBytes = iconsImage[1]
            )
        )
        val additionalType = WeatherTypeDbModel(
            code = 1000,
            dayDescription = "Sunny",
            nightDescription = "Clear",
            url = iconsUrl[0],
            iconBytes = iconsImage[0]
        )

        db.initWeatherTypes(weatherTypes)
        weatherManager.updateWeatherTypes(listOf(additionalType))
        val savedTypes = weatherManager.getWeatherTypes(listOf(1000, 1063)).getOrThrow()

        assert(savedTypes.size == 2)
        for (i in savedTypes.indices) {
            cmp(savedTypes[i], weatherTypes[i])
        }
    }

//    @Test
//    fun createUserWithIllegalPasswordTest() = runBlocking {
//        val user = UserDbModel(
//            name = "Batman",
//            email = "betsy.thebat@gmail.com",
//            password = "bat",
//            role = User.Status.Regular.name,
//            registerTimeEpoch = Calendar.getInstance().timeInMillis / 1000L,
//            id = UserDbModel.UNSPECIFIED_ID
//        )
//
//        val registerResult = db.createUser(user).getOrNull()
//        val verifiedUser = db.verifyUser(user.name, user.password).getOrNull()
//
//        assertNull(registerResult)
//        assertNull(verifiedUser)
//    }
//
//    @Test
//    fun createUserWithSpaceInNameTest() = runBlocking {
//        val user = UserDbModel(
//            name = "Spider man",
//            email = "spider.man@gmail.com",
//            password = "Spider",
//            role = User.Status.Regular.name,
//            registerTimeEpoch = Calendar.getInstance().timeInMillis / 1000L,
//            id = UserDbModel.UNSPECIFIED_ID
//        )
//
//        db.createUser(user)
//        val verifiedUser = db.verifyUser(user.name, user.password).getOrThrow()
//
//        cmp(user, verifiedUser)
//    }

    @Test
    fun insertWeatherTest(): Unit = runBlocking {
        val iconsUrl = listOf(toIconUrl(113), toIconUrl(176))
        val urls = iconsUrl.map { URL(it) }
        val iconsImage = urls.map { it.readBytes() }
        val weatherTypes = listOf(
            WeatherTypeDbModel(
                code = 1000,
                dayDescription = "Sunny",
                nightDescription = "Clear",
                url = iconsUrl[0],
                iconBytes = iconsImage[0]
            ),
            WeatherTypeDbModel(
                code = 1063,
                dayDescription = "Patchy rain possible",
                nightDescription = "Patchy rain possible",
                url = iconsUrl[1],
                iconBytes = iconsImage[1]
            )
        )
        db.initWeatherTypes(weatherTypes)
        val time1 = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_YEAR, 0)
            set(Calendar.MILLISECONDS_IN_DAY, 0)
        }.timeInMillis / 1000L
        val time2 = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_YEAR, 1)
            set(Calendar.MILLISECONDS_IN_DAY, 0)
        }.timeInMillis / 1000L

        val weather1 = WeatherDbModel(
            id = 0,
            timeEpoch = time1,
            cityId = 1,
            type = 1000,
            avgTemp = 20f,
            humidity = 20,
            windSpeed = 1,
            snowVolume = 0,
            precipitations = 0,
            vision = 10f,
            sunriseTime = 0,
            sunsetTime = 0,
            moonriseTime = 0,
            moonsetTime = 0,
            moonIllumination = 80,
            moonPhase = "Regular",
            rainChance = 10
        )
        val weather2 = WeatherDbModel(
            id = 0,
            timeEpoch = time2,
            cityId = 1,
            type = 1063,
            avgTemp = 20f,
            humidity = 60,
            windSpeed = 6,
            snowVolume = 0,
            precipitations = 0,
            vision = 10f,
            sunriseTime = 0,
            sunsetTime = 0,
            moonriseTime = 0,
            moonsetTime = 0,
            moonIllumination = 80,
            moonPhase = "Regular",
            rainChance = 60
        )

        db.saveWeather(weather1)
        db.saveWeather(weather2)
        val result = db.getDaysWeather(1, time1, time2).getOrThrow()

        assert(result.size == 2)
        cmp(result[0], weather1)
        cmp(result[1], weather1)
    }

    @Test
    fun insertWeatherWithCollisionTest(): Unit = runBlocking {
        val iconsUrl = listOf(toIconUrl(113), toIconUrl(176))
        val urls = iconsUrl.map { URL(it) }
        val iconsImage = urls.map { it.readBytes() }
        val weatherTypes = listOf(
            WeatherTypeDbModel(
                code = 1000,
                dayDescription = "Sunny",
                nightDescription = "Clear",
                url = iconsUrl[0],
                iconBytes = iconsImage[0]
            ),
            WeatherTypeDbModel(
                code = 1063,
                dayDescription = "Patchy rain possible",
                nightDescription = "Patchy rain possible",
                url = iconsUrl[1],
                iconBytes = iconsImage[1]
            )
        )
        db.initWeatherTypes(weatherTypes)
        val time = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_YEAR, 0)
            set(Calendar.MILLISECONDS_IN_DAY, 0)
        }.timeInMillis / 1000L
        val weather1 = WeatherDbModel(
            id = 0,
            timeEpoch = time,
            cityId = 1,
            type = 1000,
            avgTemp = 20f,
            humidity = 20,
            windSpeed = 1,
            snowVolume = 0,
            precipitations = 0,
            vision = 10f,
            sunriseTime = 0,
            sunsetTime = 0,
            moonriseTime = 0,
            moonsetTime = 0,
            moonIllumination = 80,
            moonPhase = "Regular",
            rainChance = 10
        )
        val weather2 = WeatherDbModel(
            id = 0,
            timeEpoch = time,
            cityId = 1,
            type = 1063,
            avgTemp = 20f,
            humidity = 60,
            windSpeed = 6,
            snowVolume = 0,
            precipitations = 0,
            vision = 10f,
            sunriseTime = 0,
            sunsetTime = 0,
            moonriseTime = 0,
            moonsetTime = 0,
            moonIllumination = 80,
            moonPhase = "Regular",
            rainChance = 60
        )

        db.saveWeather(weather1)
        db.saveWeather(weather2)
        val result = db.getDaysWeather(1, time, time + 1).getOrThrow()

        assert(result.size == 1)
        cmp(result[0], weather2)
    }

    @Test
    fun insertCurrentWeatherTest(): Unit = runBlocking {
        val iconsUrl = listOf(toIconUrl(113), toIconUrl(176))
        val urls = iconsUrl.map { URL(it) }
        val iconsImage = urls.map { it.readBytes() }
        val weatherTypes = listOf(
            WeatherTypeDbModel(
                code = 1000,
                dayDescription = "Sunny",
                nightDescription = "Clear",
                url = iconsUrl[0],
                iconBytes = iconsImage[0]
            ),
            WeatherTypeDbModel(
                code = 1063,
                dayDescription = "Patchy rain possible",
                nightDescription = "Patchy rain possible",
                url = iconsUrl[1],
                iconBytes = iconsImage[1]
            )
        )
        db.initWeatherTypes(weatherTypes)
        val time1 = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_YEAR, 0)
            set(Calendar.HOUR_OF_DAY, 2)
            set(Calendar.MILLISECONDS_IN_DAY, 0)
        }.timeInMillis / 1000L
        val time2 = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_YEAR, 0)
            set(Calendar.HOUR_OF_DAY, 3)
            set(Calendar.MILLISECONDS_IN_DAY, 0)
        }.timeInMillis / 1000L
        val dayEpoch = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_YEAR, 0)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MILLISECONDS_IN_DAY, 0)
        }.timeInMillis / 1000L

        val weather1 = CurrentWeatherDbModel(
            id = 0,
            timeEpoch = time1,
            cityId = 1,
            type = 1000,
            tempC = 20f,
            feelsLike = 15,
            humidity = 20,
            windSpeed = 2,
            snow = 0,
            precipitations = 0,
            vision = 10f,
            isDay = 1,
            cloud = 60,
        )
        val weather2 = CurrentWeatherDbModel(
            id = 0,
            timeEpoch = time2,
            cityId = 1,
            type = 1063,
            tempC = 22f,
            feelsLike = 15,
            humidity = 21,
            windSpeed = 2,
            snow = 0,
            precipitations = 0,
            vision = 15f,
            isDay = 1,
            cloud = 60,
        )

        db.saveCurrentWeather(weather1)
        db.saveCurrentWeather(weather2)
        val result = db.getCurrentWeather(1, dayEpoch).getOrThrow()

        assert(result.size == 2)
        cmp(result[0], weather1)
        cmp(result[1], weather1)
    }

    @Test
    fun insertCurrentWeatherWithCollisionTest(): Unit = runBlocking {
        val iconsUrl = listOf(toIconUrl(113), toIconUrl(176))
        val urls = iconsUrl.map { URL(it) }
        val iconsImage = urls.map { it.readBytes() }
        val weatherTypes = listOf(
            WeatherTypeDbModel(
                code = 1000,
                dayDescription = "Sunny",
                nightDescription = "Clear",
                url = iconsUrl[0],
                iconBytes = iconsImage[0]
            ),
            WeatherTypeDbModel(
                code = 1063,
                dayDescription = "Patchy rain possible",
                nightDescription = "Patchy rain possible",
                url = iconsUrl[1],
                iconBytes = iconsImage[1]
            )
        )
        db.initWeatherTypes(weatherTypes)
        val time = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_YEAR, 0)
            set(Calendar.HOUR_OF_DAY, 2)
            set(Calendar.MILLISECONDS_IN_DAY, 0)
        }.timeInMillis / 1000L
        val dayEpoch = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_YEAR, 0)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MILLISECONDS_IN_DAY, 0)
        }.timeInMillis / 1000L

        val weather1 = CurrentWeatherDbModel(
            id = 0,
            timeEpoch = time,
            cityId = 1,
            type = 1000,
            tempC = 20f,
            feelsLike = 15,
            humidity = 20,
            windSpeed = 2,
            snow = 0,
            precipitations = 0,
            vision = 10f,
            isDay = 1,
            cloud = 60,
        )
        val weather2 = CurrentWeatherDbModel(
            id = 0,
            timeEpoch = time,
            cityId = 1,
            type = 1063,
            tempC = 22f,
            feelsLike = 15,
            humidity = 21,
            windSpeed = 2,
            snow = 0,
            precipitations = 0,
            vision = 15f,
            isDay = 1,
            cloud = 60,
        )

        db.saveCurrentWeather(weather1)
        db.saveCurrentWeather(weather2)
        val result = db.getCurrentWeather(1, dayEpoch).getOrThrow()

        assert(result.size == 1)
        cmp(result[0], weather2)
    }
}