package com.softcat.database.facade

import android.content.Context
import android.icu.util.Calendar
import com.softcat.database.di.DaggerDatabaseComponent
import com.softcat.database.di.DatabaseComponent
import com.softcat.database.model.CityDbModel
import com.softcat.database.model.CountryDbModel
import com.softcat.database.model.CurrentWeatherDbModel
import com.softcat.database.model.PlotDbModel
import com.softcat.database.model.UserDbModel
import com.softcat.database.model.WeatherDbModel
import com.softcat.database.model.WeatherTypeDbModel
import javax.inject.Inject
import kotlin.math.max
import kotlin.math.min

class DatabaseProxy @Inject constructor(
    private val context: Context
): DatabaseFacade {

    private val component: DatabaseComponent by lazy {
        DaggerDatabaseComponent.factory().create(context)
    }

    private val database: DatabaseFacade by lazy {
        component.getDatabaseImplementation()
    }

    private var userRole: String? = null

    override suspend fun savePlot(model: PlotDbModel): Result<Unit> {
        if (userRole == UserDbModel.ROLE_PREMIUM)
            return database.savePlot(model)
        throw SecurityException()
    }

    override suspend fun deletePlot(model: PlotDbModel): Result<Unit> {
        if (userRole == UserDbModel.ROLE_PREMIUM)
            return database.deletePlot(model)
        throw SecurityException()
    }

    override suspend fun getPlots(userId: String): Result<List<PlotDbModel>> {
        if (userRole == UserDbModel.ROLE_PREMIUM)
            return database.getPlots(userId)
        throw SecurityException()
    }

    override suspend fun searchCity(query: String): Result<List<CityDbModel>> {
        return database.searchCity(query)
    }

    override suspend fun createUser(user: UserDbModel): Result<UserDbModel> {
        val result = database.createUser(user)
        result.onSuccess { userRole = it.role }
        return result
    }

    override suspend fun verifyUser(name: String, password: String): Result<UserDbModel> {
        val result = database.verifyUser(name, password)
        result.onSuccess { userRole = it.role }
        return result
    }

    override suspend fun addToFavourites(userId: String, cityId: Int): Result<Unit> {
        return database.addToFavourites(userId, cityId)
    }

    override suspend fun saveCountry(country: CountryDbModel): Result<Int> {
        return database.saveCountry(country)
    }

    override suspend fun saveCity(city: CityDbModel): Result<Unit> {
        return database.saveCity(city)
    }

    override suspend fun removeFromFavourites(userId: String, cityId: Int): Result<Unit> {
        return database.removeFromFavourites(userId, cityId)
    }

    override suspend fun getFavouriteCities(userId: String): Result<List<CityDbModel>> {
        return database.getFavouriteCities(userId)
    }

    override suspend fun getCountries(): Result<List<CountryDbModel>> {
        return database.getCountries()
    }

    override suspend fun updateCountries(countries: List<CountryDbModel>): Result<List<Int>> {
        return database.updateCountries(countries)
    }

    override suspend fun isFavourite(userId: String, cityId: Int): Result<Boolean> {
        return database.isFavourite(userId, cityId)
    }

    override suspend fun initWeatherTypes(weatherTypes: List<WeatherTypeDbModel>): Result<Unit> {
        return database.initWeatherTypes(weatherTypes)
    }

    override suspend fun saveWeather(model: WeatherDbModel): Result<Unit> {
        return database.saveWeather(model)
    }

    override suspend fun saveCurrentWeather(model: CurrentWeatherDbModel): Result<Unit> {
        return database.saveCurrentWeather(model)
    }

    override suspend fun getCurrentWeather(
        cityId: Int,
        dayTimeEpoch: Long
    ): Result<List<CurrentWeatherDbModel>> {
        if (userRole == UserDbModel.ROLE_PREMIUM)
            return database.getCurrentWeather(cityId, dayTimeEpoch)

        val yesterdayTime = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_YEAR, get(Calendar.DAY_OF_YEAR) - 1)
            set(Calendar.MILLISECONDS_IN_DAY, 0)
        }.timeInMillis / 1000L

        if (userRole == UserDbModel.ROLE_FOLLOWER) {
            return if (yesterdayTime <= dayTimeEpoch)
                database.getCurrentWeather(cityId, dayTimeEpoch)
            else
                Result.failure(SecurityException())
        }

        val tomorrowTime = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_YEAR, get(Calendar.DAY_OF_YEAR) + 1)
            set(Calendar.MILLISECONDS_IN_DAY, 0)
        }.timeInMillis / 1000L

        return if (dayTimeEpoch in yesterdayTime..tomorrowTime)
            database.getCurrentWeather(cityId, dayTimeEpoch)
        else
            Result.failure(SecurityException())
    }

    override suspend fun getDaysWeather(
        cityId: Int,
        startSeconds: Long,
        endSeconds: Long
    ): Result<List<WeatherDbModel>> {

        if (userRole == UserDbModel.ROLE_PREMIUM)
            return database.getDaysWeather(cityId, startSeconds, endSeconds)

        val yesterdayTime = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_YEAR, get(Calendar.DAY_OF_YEAR) - 1)
            set(Calendar.MILLISECONDS_IN_DAY, 0)
        }.timeInMillis / 1000L

        if (userRole == UserDbModel.ROLE_FOLLOWER)
            return database.getDaysWeather(cityId, max(yesterdayTime, startSeconds), endSeconds)

        val tomorrowTime by lazy {
            Calendar.getInstance().apply {
                set(Calendar.DAY_OF_YEAR, get(Calendar.DAY_OF_YEAR) + 1)
                set(Calendar.MILLISECONDS_IN_DAY, 0)
            }.timeInMillis / 1000L
        }

        return database.getDaysWeather(
            cityId,
            max(yesterdayTime, startSeconds),
            min(tomorrowTime, endSeconds)
        )
    }

    override suspend fun getWeatherTypes(typeCodes: List<Int>): Result<List<WeatherTypeDbModel>> {
        return database.getWeatherTypes(typeCodes)
    }

}