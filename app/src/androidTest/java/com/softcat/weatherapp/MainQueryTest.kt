package com.softcat.weatherapp

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.softcat.database.model.CityDbModel
import com.softcat.database.model.CountryDbModel
import com.softcat.database.model.UserDbModel
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.math.abs

@RunWith(AndroidJUnit4::class)
class MainQueryTest {
    private val context = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext
    private val application = context as WeatherApplication
    private val component = DaggerAndroidTestsComponent.factory().create(context, application.dataStore)
    private val db = component.getDatabaseImpl()
    private val usersManager = component.getUsersManager()
    private val regionManager = component.getRegionManager()

    private var _user: UserDbModel? = null
    private val user: UserDbModel
        get() = _user!!

    private fun cmp(a: CityDbModel, b: CityDbModel) {
        assert(a.id == b.id)
        assert(a.name == b.name)
        assert(abs(a.latitude - b.latitude) < 1e-9)
        assert(abs(a.longitude - b.longitude) < 1e-9)
        assert(a.countryId == b.countryId)
    }

    @Before
    fun registerUser() {
        runBlocking {
            _user = usersManager.logIn("Test User", "123456").getOrThrow()
        }
    }

    @Test
    fun addToFavouritesDifferentCountries(): Unit = runBlocking {
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

        val countriesIds = countries.map { db.saveCountry(it).getOrThrow() }
        cities.forEach { db.saveCity(it) }
        db.addToFavourites(user.id, cities[0].id)
        db.addToFavourites(user.id, cities[1].id)
        val result = db.getFavouriteCities(user.id).getOrThrow()
        cities.forEach {
            db.removeFromFavourites(user.id, it.id)
            regionManager.deleteCity(it.id)
        }
        countriesIds.forEach { regionManager.deleteCountry(it) }

        assert(result.size == 2)
        cmp(cities[0], result[0])
        cmp(cities[1], result[1])
    }

    @Test
    fun addToFavouritesSameCountries(): Unit = runBlocking {
        val country = CountryDbModel(
            id = 1,
            name = "Russia"
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
                name = "Voronezh",
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

        val countryId = db.saveCountry(country).getOrThrow()
        cities.forEach { db.saveCity(it) }
        db.addToFavourites(user.id, cities[0].id)
        db.addToFavourites(user.id, cities[1].id)
        val result = db.getFavouriteCities(user.id).getOrThrow()
        cities.forEach {
            db.removeFromFavourites(user.id, it.id)
            regionManager.deleteCity(it.id)
        }
        regionManager.deleteCountry(countryId)

        assert(result.size == 2)
        cmp(cities[0], result[0])
        cmp(cities[1], result[1])
    }

    @Test
    fun doubleAddToFavourites(): Unit = runBlocking {
        val country = CountryDbModel(
            id = 1,
            name = "Russia"
        )
        val city = CityDbModel(
            id = 1,
            name = "Moscow",
            countryId = 1,
            latitude = 12f,
            longitude = 39f
        )
        val countryId = db.saveCountry(country).getOrThrow()
        db.saveCity(city)
        db.addToFavourites(user.id, city.id)
        val result = db.getFavouriteCities(user.id).getOrThrow()
        db.removeFromFavourites(user.id, city.id)
        regionManager.deleteCity(city.id)
        regionManager.deleteCity(countryId)

        assert(result.size == 1)
        cmp(city, result[0])
    }

    @Test
    fun removeFromFavourites(): Unit = runBlocking {
        val country = CountryDbModel(
            id = 1,
            name = "Russia"
        )
        val city = CityDbModel(
            id = 1,
            name = "Moscow",
            countryId = 1,
            latitude = 12f,
            longitude = 39f
        )

        db.saveCountry(country)
        db.saveCity(city)
        db.addToFavourites(user.id, city.id)
        db.removeFromFavourites(user.id, city.id)
        val result = db.getFavouriteCities(user.id).getOrThrow()
        regionManager.deleteCity(city.id)
        regionManager.deleteCountry(country.id)

        assert(result.isEmpty())
    }

    @Test
    fun pseudoRemoveFromFavourites(): Unit = runBlocking {
        val country = CountryDbModel(
            id = 1,
            name = "Russia"
        )
        val city = CityDbModel(
            id = 1,
            name = "Moscow",
            countryId = 1,
            latitude = 12f,
            longitude = 39f
        )
        val extraCity = CityDbModel(
            id = 2,
            name = "Peter",
            countryId = 1,
            latitude = 3f,
            longitude = -4.5f
        )
        db.saveCountry(country)
        db.saveCity(city)
        db.saveCity(extraCity)
        db.addToFavourites(user.id, city.id)
        db.removeFromFavourites(user.id + "1", city.id)
        val result = db.getFavouriteCities(user.id).getOrThrow()
        db.removeFromFavourites(user.id, city.id)
        db.removeFromFavourites(user.id, extraCity.id)
        regionManager.deleteCity(city.id)
        regionManager.deleteCity(extraCity.id)
        regionManager.deleteCountry(country.id)

        assert(result.size == 1)
        cmp(city, result[0])
    }

    @Test
    fun getFromFavouritesWhenEmpty(): Unit = runBlocking {
        val result = db.getFavouriteCities(user.id).getOrThrow()
        assert(result.isEmpty())
    }

    @Test
    fun getFromFavourites(): Unit = runBlocking {
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
                id = 3,
                name = "Voronezh",
                countryId = 1,
                latitude = -3f,
                longitude = 2.5f
            )
        )
        countries.forEach { db.saveCountry(it) }
        cities.forEach {
            db.saveCity(it)
            db.addToFavourites(user.id, it.id)
        }
        val emptyResult = db.getFavouriteCities(user.id + "1").getOrThrow()
        val result = db.getFavouriteCities(user.id).getOrThrow()
        cities.forEach {
            db.removeFromFavourites(user.id, it.id)
        }

        assert(emptyResult.isEmpty())
        assert(result.size == 3)
        cmp(cities[0], result[0])
        cmp(cities[1], result[1])
        cmp(cities[2], result[2])
    }

    @Test
    fun getFromFavouritesWhenNotAllSaved(): Unit = runBlocking {
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
                id = 3,
                name = "Voronezh",
                countryId = 1,
                latitude = -3f,
                longitude = 2.5f
            ),
            CityDbModel(
                id = 100,
                name = "Edinburgh",
                countryId = 2,
                latitude = 2f,
                longitude = 3f
            )
        )

        countries.forEach { db.saveCountry(it) }
        db.saveCity(cities[0])
        db.saveCity(cities[2])
        db.addToFavourites(user.id, cities[0].id)
        db.addToFavourites(user.id, cities[2].id)
        db.addToFavourites(user.id, cities[3].id)
        val result = db.getFavouriteCities(user.id).getOrThrow()
        cities.forEach { db.removeFromFavourites(user.id, it.id) }

        assert(result.size == 2)
        cmp(cities[0], result[0])
        cmp(cities[2], result[1])
    }

    @Test
    fun getUniqueFromFavouritesWhenNotSaved(): Unit = runBlocking {
        val country = CountryDbModel(
            id = 1,
            name = "Russia"
        )
        val city = CityDbModel(
            id = 1,
            name = "Moscow",
            countryId = 1,
            latitude = 12f,
            longitude = 39f
        )

        db.saveCountry(country)
        db.addToFavourites(user.id, city.id)
        val result = db.getFavouriteCities(user.id).getOrThrow()
        db.removeFromFavourites(user.id, city.id)
        regionManager.deleteCountry(country.id)

        assert(result.isEmpty())
    }
}