package com.softcat.database.commands.factory

import com.softcat.database.commands.AddCityCommand
import com.softcat.database.commands.AddCountryCommand
import com.softcat.database.commands.AddCurrentWeatherCommand
import com.softcat.database.commands.AddToFavouritesCommand
import com.softcat.database.commands.AddWeatherCommand
import com.softcat.database.commands.ClearWeatherDataCommand
import com.softcat.database.commands.CreateUserCommand
import com.softcat.database.commands.GetCountriesCommand
import com.softcat.database.commands.GetCurrentWeatherCommand
import com.softcat.database.commands.GetDayWeatherCommand
import com.softcat.database.commands.GetFavouriteCitiesCommand
import com.softcat.database.commands.GetPlotCommand
import com.softcat.database.commands.GetWeatherTypesCommand
import com.softcat.database.commands.IsFavouriteCommand
import com.softcat.database.commands.RemoveFromFavouritesCommand
import com.softcat.database.commands.SavePlotCommand
import com.softcat.database.commands.SearchCityCommand
import com.softcat.database.commands.UpdateCountriesCommand
import com.softcat.database.commands.UpdateWeatherTypesCommand
import com.softcat.database.commands.VerifyUserCommand
import com.softcat.database.managers.local.PlotManager
import com.softcat.database.managers.local.region.RegionManager
import com.softcat.database.managers.local.weather.WeatherManager
import com.softcat.database.managers.remote.favourites.FavouritesManager
import com.softcat.database.managers.remote.user.UsersManager
import com.softcat.database.model.CityDbModel
import com.softcat.database.model.CountryDbModel
import com.softcat.database.model.CurrentWeatherDbModel
import com.softcat.database.model.PlotDbModel
import com.softcat.database.model.UserDbModel
import com.softcat.database.model.WeatherDbModel
import com.softcat.database.model.WeatherTypeDbModel
import javax.inject.Inject

class CommandFactory @Inject constructor(
    private val usersManager: UsersManager,
    private val favouritesManager: FavouritesManager,
    private val regionManager: RegionManager,
    private val weatherManager: WeatherManager,
    private val plotManager: PlotManager
): CommandFactoryInterface {

    override fun savePlotCommand(model: PlotDbModel) = SavePlotCommand(model, plotManager)

    override fun getPlotCommand(userId: String) = GetPlotCommand(userId, plotManager)

    override fun searchCityCommand(query: String): SearchCityCommand {
        return SearchCityCommand(regionManager, query)
    }

    override fun createUserCommand(user: UserDbModel): CreateUserCommand {
        return CreateUserCommand(
            user,
            usersManager
        )
    }

    override fun getFavouriteCitiesCommand(userId: String): GetFavouriteCitiesCommand {
        return GetFavouriteCitiesCommand(
            userId,
            favouritesManager,
            regionManager
        )
    }

    override fun addCountryCommand(country: CountryDbModel) = AddCountryCommand(country, regionManager)

    override fun addCityCommand(city: CityDbModel) = AddCityCommand(city, regionManager)

    override fun getCountriesCommand() = GetCountriesCommand(regionManager)

    override fun updateCountriesCommand(countries: List<CountryDbModel>): UpdateCountriesCommand {
        return UpdateCountriesCommand(countries, regionManager)
    }

    override fun verifyUserCommand(name: String, password: String): VerifyUserCommand {
        return VerifyUserCommand(name, password, usersManager)
    }

    override fun addToFavouriteCommand(userId: String, cityId: Int): AddToFavouritesCommand {
        return AddToFavouritesCommand(userId, cityId, favouritesManager)
    }

    override fun removeFromFavouriteCommand(
        userId: String,
        cityId: Int
    ): RemoveFromFavouritesCommand {
        return RemoveFromFavouritesCommand(userId, cityId, favouritesManager)
    }

    override fun isFavouriteCommand(userId: String, cityId: Int): IsFavouriteCommand {
        return IsFavouriteCommand(userId, cityId, favouritesManager)
    }

    override fun addWeatherCommand(model: WeatherDbModel): AddWeatherCommand {
        return AddWeatherCommand(model, weatherManager)
    }

    override fun addCurrentWeatherCommand(model: CurrentWeatherDbModel): AddCurrentWeatherCommand {
        return AddCurrentWeatherCommand(model, weatherManager)
    }

    override fun updateWeatherTypesCommand(types: List<WeatherTypeDbModel>): UpdateWeatherTypesCommand {
        return UpdateWeatherTypesCommand(types, weatherManager)
    }

    override fun getCurrentWeatherCommand(
        cityId: Int,
        dayTimeEpoch: Long
    ) = GetCurrentWeatherCommand(cityId, dayTimeEpoch, weatherManager)

    override fun getDaysWeatherCommand(
        cityId: Int,
        startMillis: Long,
        endMillis: Long
    ) = GetDayWeatherCommand(cityId, startMillis, endMillis, weatherManager)

    override fun getWeatherTypesCommand(typeCodes: List<Int>) = GetWeatherTypesCommand(typeCodes, weatherManager)

    override fun getClearWeatherDataCommand() = ClearWeatherDataCommand(weatherManager)
}