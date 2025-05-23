package com.softcat.database.commands.factory

import com.softcat.database.commands.AddCityCommand
import com.softcat.database.commands.AddCountryCommand
import com.softcat.database.commands.AddCurrentWeatherCommand
import com.softcat.database.commands.AddToFavouritesCommand
import com.softcat.database.commands.AddWeatherCommand
import com.softcat.database.commands.CreateUserCommand
import com.softcat.database.commands.GetCountriesCommand
import com.softcat.database.commands.GetCurrentWeatherCommand
import com.softcat.database.commands.GetDayWeatherCommand
import com.softcat.database.commands.GetFavouriteCitiesCommand
import com.softcat.database.commands.GetWeatherTypesCommand
import com.softcat.database.commands.IsFavouriteCommand
import com.softcat.database.commands.RemoveFromFavouritesCommand
import com.softcat.database.commands.UpdateCountriesCommand
import com.softcat.database.commands.UpdateWeatherTypesCommand
import com.softcat.database.commands.VerifyUserCommand
import com.softcat.database.managers.ManagerFactoryInterface
import com.softcat.database.managers.local.weather.WeatherManager
import com.softcat.database.managers.remote.favourites.FavouritesManager
import com.softcat.database.managers.remote.user.UsersManager
import com.softcat.database.model.CityDbModel
import com.softcat.database.model.CountryDbModel
import com.softcat.database.model.CurrentWeatherDbModel
import com.softcat.database.model.WeatherDbModel
import com.softcat.database.model.WeatherTypeDbModel
import javax.inject.Inject

class CommandFactory @Inject constructor(
    private val usersManager: UsersManager,
    private val favouritesManager: FavouritesManager,
    private val managerFactory: ManagerFactoryInterface,
    private val weatherManager: WeatherManager,
): CommandFactoryInterface {

    override fun createUserCommand(name: String, email: String, password: String): CreateUserCommand {
        return CreateUserCommand(
            name,
            email,
            password,
            usersManager
        )
    }

    override fun getFavouriteCitiesCommand(userId: String): GetFavouriteCitiesCommand {
        return GetFavouriteCitiesCommand(
            userId,
            favouritesManager,
            managerFactory
        )
    }

    override fun addCountryCommand(country: CountryDbModel) = AddCountryCommand(country, managerFactory)

    override fun addCityCommand(city: CityDbModel) = AddCityCommand(city, managerFactory)

    override fun getCountriesCommand() = GetCountriesCommand(managerFactory)

    override fun updateCountriesCommand(countries: List<CountryDbModel>): UpdateCountriesCommand {
        return UpdateCountriesCommand(countries, managerFactory)
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
}