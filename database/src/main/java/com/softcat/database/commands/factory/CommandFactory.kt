package com.softcat.database.commands.factory

import com.softcat.database.commands.AddCityCommand
import com.softcat.database.commands.AddCountryCommand
import com.softcat.database.commands.AddToFavouritesCommand
import com.softcat.database.commands.CreateUserCommand
import com.softcat.database.commands.GetCountriesCommand
import com.softcat.database.commands.GetFavouriteCitiesCommand
import com.softcat.database.commands.IsFavouriteCommand
import com.softcat.database.commands.RemoveFromFavouritesCommand
import com.softcat.database.commands.UpdateCountriesCommand
import com.softcat.database.commands.VerifyUserCommand
import com.softcat.database.managers.ManagerFactory
import com.softcat.database.managers.ManagerFactoryInterface
import com.softcat.database.managers.local.region.RegionManager
import com.softcat.database.managers.remote.favourites.FavouritesManager
import com.softcat.database.managers.remote.user.UsersManager
import com.softcat.database.model.CityDbModel
import com.softcat.database.model.CountryDbModel
import javax.inject.Inject

class CommandFactory @Inject constructor(
    private val usersManager: UsersManager,
    private val favouritesManager: FavouritesManager,
    private val managerFactory: ManagerFactoryInterface
): CommandFactoryInterface {

    override fun createUserCommand(name: String, email: String, password: String): CreateUserCommand {
        return CreateUserCommand(
            name,
            email,
            password,
            usersManager
        )
    }

    override fun getFavouriteCitiesCommand(userId: Int): GetFavouriteCitiesCommand {
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

    override fun addToFavouriteCommand(userId: Int, cityId: Int): AddToFavouritesCommand {
        return AddToFavouritesCommand(userId, cityId, favouritesManager)
    }

    override fun removeFromFavouriteCommand(userId: Int, cityId: Int): RemoveFromFavouritesCommand {
        return RemoveFromFavouritesCommand(userId, cityId, favouritesManager)
    }

    override fun isFavouriteCommand(userId: Int, cityId: Int): IsFavouriteCommand {
        return IsFavouriteCommand(userId, cityId, favouritesManager)
    }
}