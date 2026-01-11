package com.softcat.database.facade

import com.softcat.database.commands.factory.CommandFactoryInterface
import com.softcat.database.exceptions.NoCommandResultException
import com.softcat.database.model.CityDbModel
import com.softcat.database.model.CountryDbModel
import com.softcat.database.model.CurrentWeatherDbModel
import com.softcat.database.model.PlotDbModel
import com.softcat.database.model.UserAvatarDbModel
import com.softcat.database.model.UserDbModel
import com.softcat.database.model.WeatherDbModel
import com.softcat.database.model.WeatherTypeDbModel
import javax.inject.Inject

class DatabaseFacadeImpl @Inject constructor(
    private val factory: CommandFactoryInterface
): DatabaseFacade {

    override suspend fun savePlot(model: PlotDbModel): Result<Unit> {
        val cmd = factory.savePlotCommand(model)
        cmd.execute()
        return cmd.result ?: Result.failure(NoCommandResultException())
    }

    override suspend fun deletePlot(model: PlotDbModel): Result<Unit> {
        val cmd = factory.savePlotCommand(model)
        cmd.rollback()
        return cmd.result ?: Result.failure(NoCommandResultException())
    }

    override suspend fun getPlots(userId: String): Result<List<PlotDbModel>> {
        val cmd = factory.getPlotCommand(userId)
        cmd.execute()
        return cmd.result ?: Result.failure(NoCommandResultException())
    }

    override suspend fun searchCity(query: String): Result<List<CityDbModel>> {
        val cmd = factory.searchCityCommand(query)
        cmd.execute()
        return cmd.result ?: Result.failure(NoCommandResultException())
    }

    override suspend fun createUser(user: UserDbModel): Result<UserDbModel> {
        val cmd = factory.createUserCommand(user)
        cmd.execute()
        return cmd.result ?: Result.failure(NoCommandResultException())
    }

    override suspend fun verifyUser(name: String, password: String): Result<UserDbModel> {
        val cmd = factory.verifyUserCommand(name, password)
        cmd.execute()
        return cmd.result ?: Result.failure(NoCommandResultException())
    }

    override suspend fun addToFavourites(userId: String, cityId: Int): Result<Unit> {
        val cmd = factory.addToFavouriteCommand(userId, cityId)
        cmd.execute()
        return cmd.result ?: Result.failure(NoCommandResultException())
    }

    override suspend fun saveCountry(country: CountryDbModel): Result<Int> {
        val cmd = factory.addCountryCommand(country)
        cmd.execute()
        return cmd.result ?: Result.failure(NoCommandResultException())
    }

    override suspend fun saveCity(city: CityDbModel): Result<Unit> {
        val cmd = factory.addCityCommand(city)
        cmd.execute()
        return cmd.result ?: Result.failure(NoCommandResultException())
    }

    override suspend fun removeFromFavourites(userId: String, cityId: Int): Result<Unit> {
        val cmd = factory.removeFromFavouriteCommand(userId, cityId)
        cmd.execute()
        return cmd.result ?: Result.failure(NoCommandResultException())
    }

    override suspend fun getFavouriteCities(userId: String): Result<List<CityDbModel>> {
        val cmd = factory.getFavouriteCitiesCommand(userId)
        cmd.execute()
        return cmd.result ?: Result.failure(NoCommandResultException())
    }

    override suspend fun getCountries(): Result<List<CountryDbModel>> {
        val cmd = factory.getCountriesCommand()
        cmd.execute()
        return cmd.result ?: Result.failure(NoCommandResultException())
    }

    override suspend fun updateCountries(countries: List<CountryDbModel>): Result<List<Int>> {
        val cmd = factory.updateCountriesCommand(countries)
        cmd.execute()
        return cmd.result ?: Result.failure(NoCommandResultException())
    }

    override suspend fun isFavourite(userId: String, cityId: Int): Result<Boolean> {
        val cmd = factory.isFavouriteCommand(userId, cityId)
        cmd.execute()
        return cmd.result ?: Result.failure(NoCommandResultException())
    }

    override suspend fun initWeatherTypes(weatherTypes: List<WeatherTypeDbModel>): Result<Unit> {
        val cmd = factory.updateWeatherTypesCommand(weatherTypes)
        cmd.execute()
        return cmd.result ?: Result.failure(NoCommandResultException())
    }

    override suspend fun saveWeather(model: WeatherDbModel): Result<Unit> {
        val cmd = factory.addWeatherCommand(model)
        cmd.execute()
        return cmd.result ?: Result.failure(NoCommandResultException())
    }

    override suspend fun saveCurrentWeather(model: CurrentWeatherDbModel): Result<Unit> {
        val cmd = factory.addCurrentWeatherCommand(model)
        cmd.execute()
        return cmd.result ?: Result.failure(NoCommandResultException())
    }

    override suspend fun getCurrentWeather(
        cityId: Int,
        dayTimeEpoch: Long
    ): Result<List<CurrentWeatherDbModel>> {
        val cmd = factory.getCurrentWeatherCommand(cityId, dayTimeEpoch)
        cmd.execute()
        return cmd.result ?: Result.failure(NoCommandResultException())
    }

    override suspend fun getDaysWeather(
        cityId: Int,
        startSeconds: Long,
        endSeconds: Long
    ): Result<List<WeatherDbModel>> {
        val cmd = factory.getDaysWeatherCommand(cityId, startSeconds, endSeconds)
        cmd.execute()
        return cmd.result ?: Result.failure(NoCommandResultException())
    }

    override suspend fun getWeatherTypes(typeCodes: List<Int>): Result<List<WeatherTypeDbModel>> {
        val cmd = factory.getWeatherTypesCommand(typeCodes)
        cmd.execute()
        return cmd.result ?: Result.failure(NoCommandResultException())
    }

    override suspend fun clearWeatherData(): Result<Unit> {
        val cmd = factory.getClearWeatherDataCommand()
        cmd.execute()
        return cmd.result
    }

    override suspend fun weatherTypesLoaded(): Result<Boolean> {
        val cmd = factory.getWeatherTypesLoadedCommand()
        cmd.execute()
        return cmd.result
    }

    override suspend fun updateAvatar(
        userId: String,
        avatar: UserAvatarDbModel
    ): Result<Unit> {
        val cmd = factory.getUpdateAvatarCommand(userId, avatar)
        cmd.execute()
        return cmd.result
    }

    override suspend fun getAvatar(userId: String): Result<UserAvatarDbModel> {
        val cmd = factory.getReadAvatarCommand(userId)
        cmd.execute()
        return cmd.result
    }
}