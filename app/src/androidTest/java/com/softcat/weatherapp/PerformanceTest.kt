package com.softcat.weatherapp

import android.icu.util.Calendar
import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.softcat.database.model.CityDbModel
import com.softcat.database.model.CountryDbModel
import com.softcat.database.model.WeatherDbModel
import com.softcat.database.model.WeatherTypeDbModel
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.random.Random
import kotlin.time.measureTime

@RunWith(AndroidJUnit4::class)
class PerformanceTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext
    private val component = DaggerUnitTestsComponent.factory().create(context)
    private val db = component.getDatabase()
    private val regionManager = component.getRegionManager()

    private suspend fun getDayWeatherMillis(n: Int): Float {
        val country = CountryDbModel(
            id = CountryDbModel.UNSPECIFIED_ID,
            name = "Russia"
        )
        val city = CityDbModel(
            id = 2145091,
            name = "Moscow",
            countryId = 1,
            latitude = 55.75f,
            longitude = 37.62f
        )
        val weatherType = WeatherTypeDbModel(
            code = 1000,
            dayDescription = "Sunny",
            nightDescription = "Clearing",
            url = "",
            iconBytes = null
        )
        db.saveCountry(country)
        db.saveCity(city)
        db.initWeatherTypes(listOf(weatherType))
        val timeEpochs = List(n) {
            Calendar.getInstance().apply {
                set(Calendar.DAY_OF_YEAR, it)
                set(Calendar.MILLISECONDS_IN_DAY, 0)
            }.timeInMillis / 1000L
        }
        val weatherList = List(n) {
            WeatherDbModel(
                id = 1,
                timeEpoch = timeEpochs[it],
                cityId = 2145091,
                type = weatherType.code,
                avgTemp = Random.nextInt(10, 20).toFloat(),
                humidity = 80,
                windSpeed = Random.nextInt(0, 5),
                snowVolume = 0,
                precipitations = 0,
                vision = 10f,
                sunriseTime = 0,
                sunsetTime = 0,
                moonriseTime = 0,
                moonsetTime = 0,
                moonIllumination = 0,
                moonPhase = "",
                rainChance = 0
            )
        }
        weatherList.forEach { db.saveWeather(it) }
        val selections = List(n) {
            val l = Random.nextInt(0, n / 2)
            val r = l + n / 4
            l to r
        }
        val time = measureTime {
            for (i in 0 until n) {
                val (l, r) = selections[i]
                db.getDaysWeather(2145091, timeEpochs[l], timeEpochs[r])
            }
        }
        return time.inWholeNanoseconds.toFloat() / 1e6f
    }

    private suspend fun insertDayWeatherMillis(n: Int): Float {
        val country = CountryDbModel(
            id = CountryDbModel.UNSPECIFIED_ID,
            name = "Russia"
        )
        val city = CityDbModel(
            id = 2145091,
            name = "Moscow",
            countryId = 1,
            latitude = 55.75f,
            longitude = 37.62f
        )
        val weatherType = WeatherTypeDbModel(
            code = 1000,
            dayDescription = "Sunny",
            nightDescription = "Clearing",
            url = "",
            iconBytes = null
        )
        db.saveCountry(country)
        db.saveCity(city)
        db.initWeatherTypes(listOf(weatherType))
        val timeEpochs = List(n) {
            Calendar.getInstance().apply {
                set(Calendar.DAY_OF_YEAR, it)
                set(Calendar.MILLISECONDS_IN_DAY, 0)
            }.timeInMillis / 1000L
        }
        val weatherList = List(n / 2) {
            WeatherDbModel(
                id = 1,
                timeEpoch = timeEpochs[it],
                cityId = 2145091,
                type = weatherType.code,
                avgTemp = Random.nextInt(10, 20).toFloat(),
                humidity = 80,
                windSpeed = Random.nextInt(0, 5),
                snowVolume = 0,
                precipitations = 0,
                vision = 10f,
                sunriseTime = 0,
                sunsetTime = 0,
                moonriseTime = 0,
                moonsetTime = 0,
                moonIllumination = 0,
                moonPhase = "",
                rainChance = 0
            )
        }
        val time = measureTime {
            weatherList.forEach { db.saveWeather(it) }
            weatherList.forEach { db.saveWeather(it) }
        }
        return time.inWholeNanoseconds.toFloat() / 1e6f
    }

    private suspend fun insertCountriesMillis(count: Int, iterations: Int): Float {
        val countries = List(count) {
            CountryDbModel(
                id = 1,
                name = "country_" + Random.nextInt(1, 10000)
            )
        }
        val time = measureTime {
            for (i in 1..iterations) {
                db.updateCountries(countries)
            }
        }
        countries.forEach { regionManager.deleteCountry(it.id) }

        return time.inWholeNanoseconds.toFloat() / 1e6f
    }

    @Test
    fun getDayWeatherPerformanceTest(): Unit = runBlocking {
        val measurements = List(20) {
            getDayWeatherMillis(5000)
        }
        Log.i(LOG_TAG, "Get weather measurements: $measurements.")
    }

    @Test
    fun insertDayWeatherPerformanceTest(): Unit = runBlocking {
        val measurements = List(40) {
            insertDayWeatherMillis(100)
        }
        Log.i(LOG_TAG, "Insert weather measurements: $measurements.")
    }

    @Test
    fun saveCountryPerformanceTest(): Unit = runBlocking {
        val measurements = List(40) {
            insertCountriesMillis(20, 30)
        }
        Log.i(LOG_TAG, "Save country measurements: $measurements.")
    }

    companion object {
        private const val LOG_TAG = "mumu"
    }
}