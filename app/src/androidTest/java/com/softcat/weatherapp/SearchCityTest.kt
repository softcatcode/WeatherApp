package com.softcat.weatherapp

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.softcat.database.model.CityDbModel
import com.softcat.database.model.CountryDbModel
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.math.abs

@RunWith(AndroidJUnit4::class)
class SearchCityTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext
    private val application = context as WeatherApplication
    private val component = DaggerAndroidTestsComponent.factory().create(context, application.dataStore)
    private val db = component.getDatabaseImpl()
    private val regionManager = component.getRegionManager()

    private fun cmp(a: CityDbModel, b: CityDbModel) {
        assert(a.id == b.id)
        assert(a.name == b.name)
        assert(abs(a.latitude - b.latitude) < 1e-9)
        assert(abs(a.longitude - b.longitude) < 1e-9)
        assert(a.countryId == b.countryId)
    }

    @Test
    fun searchWhenEmpty(): Unit = runBlocking {
        val result = db.searchCity("Mos").getOrThrow()
        assert(result.isEmpty())
    }

    @Test
    fun searchMultipleResult(): Unit = runBlocking {
        val countries = listOf(
            CountryDbModel(
                id = 1,
                name = "Russia"
            ),
            CountryDbModel(
                id = 2,
                name = "England"
            )
        )
        val cities = listOf(
            CityDbModel(
                id = 1,
                name = "Moscow",
                countryId = 1,
                latitude = 12f,
                longitude = 39f
            ),
            CityDbModel(
                id = 2,
                name = "London",
                countryId = 2,
                latitude = 3f,
                longitude = -4.5f
            ),
            CityDbModel(
                id = 100,
                name = "Edinburgh",
                countryId = 2,
                latitude = 2f,
                longitude = 3f
            )
        )
        val countryIds = countries.map { db.saveCountry(it).getOrThrow() }
        cities.forEach { db.saveCity(it) }
        val result = db.searchCity("o").getOrThrow()
        cities.forEach { regionManager.deleteCity(it.id) }
        countryIds.forEach { regionManager.deleteCity(it) }

        assert(result.size == 2)
        cmp(cities[0], result[0])
        cmp(cities[1], result[1])
    }

    @Test
    fun searchUniqueResult(): Unit = runBlocking {
        val countries = listOf(
            CountryDbModel(
                id = 1,
                name = "Russia"
            ),
            CountryDbModel(
                id = 2,
                name = "England"
            )
        )
        val cities = listOf(
            CityDbModel(
                id = 1,
                name = "Moscow",
                countryId = 1,
                latitude = 12f,
                longitude = 39f
            ),
            CityDbModel(
                id = 2,
                name = "London",
                countryId = 2,
                latitude = 3f,
                longitude = -4.5f
            ),
            CityDbModel(
                id = 100,
                name = "Edinburgh",
                countryId = 2,
                latitude = 2f,
                longitude = 3f
            )
        )
        val countryIds = countries.map { db.saveCountry(it).getOrThrow() }
        cities.forEach { db.saveCity(it) }
        val result = db.searchCity("Mos").getOrThrow()
        cities.forEach { regionManager.deleteCity(it.id) }
        countryIds.forEach { regionManager.deleteCity(it) }

        assert(result.size == 1)
        cmp(cities[0], result[0])
    }

    @Test
    fun searchEmptyResultFullRequest(): Unit = runBlocking {
        val countries = listOf(
            CountryDbModel(
                id = 1,
                name = "Russia"
            ),
            CountryDbModel(
                id = 2,
                name = "England"
            )
        )
        val cities = listOf(
            CityDbModel(
                id = 1,
                name = "Moscow",
                countryId = 1,
                latitude = 12f,
                longitude = 39f
            ),
            CityDbModel(
                id = 2,
                name = "London",
                countryId = 2,
                latitude = 3f,
                longitude = -4.5f
            ),
            CityDbModel(
                id = 100,
                name = "Edinburgh",
                countryId = 2,
                latitude = 2f,
                longitude = 3f
            )
        )
        val countryIds = countries.map { db.saveCountry(it).getOrThrow() }
        cities.forEach { db.saveCity(it) }
        val result = db.searchCity("Unknown").getOrThrow()
        cities.forEach { regionManager.deleteCity(it.id) }
        countryIds.forEach { regionManager.deleteCity(it) }

        assert(result.isEmpty())
    }

    @Test
    fun searchEmptyRequestFullDb(): Unit = runBlocking {
        val countries = listOf(
            CountryDbModel(
                id = 1,
                name = "Russia"
            ),
            CountryDbModel(
                id = 2,
                name = "England"
            )
        )
        val cities = listOf(
            CityDbModel(
                id = 1,
                name = "Moscow",
                countryId = 1,
                latitude = 12f,
                longitude = 39f
            ),
            CityDbModel(
                id = 2,
                name = "London",
                countryId = 2,
                latitude = 3f,
                longitude = -4.5f
            ),
            CityDbModel(
                id = 100,
                name = "Edinburgh",
                countryId = 2,
                latitude = 2f,
                longitude = 3f
            )
        )
        val countryIds = countries.map { db.saveCountry(it).getOrThrow() }
        cities.forEach { db.saveCity(it) }
        val result = db.searchCity("").getOrThrow()
        cities.forEach { regionManager.deleteCity(it.id) }
        countryIds.forEach { regionManager.deleteCity(it) }

        assert(result.isEmpty())
    }

    @Test
    fun emptyRequestEmptyDb(): Unit = runBlocking {
        val result = db.searchCity("").getOrThrow()
        assert(result.isEmpty())
    }
}