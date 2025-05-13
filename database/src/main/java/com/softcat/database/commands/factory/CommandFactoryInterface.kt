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
import com.softcat.database.model.CityDbModel
import com.softcat.database.model.CountryDbModel

interface CommandFactoryInterface {
    fun createUserCommand(name: String, email: String, password: String): CreateUserCommand

    fun getFavouriteCitiesCommand(userId: Int): GetFavouriteCitiesCommand

    fun addCountryCommand(country: CountryDbModel): AddCountryCommand

    fun addCityCommand(city: CityDbModel): AddCityCommand

    fun getCountriesCommand(): GetCountriesCommand

    fun updateCountriesCommand(countries: List<CountryDbModel>): UpdateCountriesCommand

    fun verifyUserCommand(name: String, password: String): VerifyUserCommand

    fun addToFavouriteCommand(userId: Int, cityId: Int): AddToFavouritesCommand

    fun removeFromFavouriteCommand(userId: Int, cityId: Int): RemoveFromFavouritesCommand

    fun isFavouriteCommand(userId: Int, cityId: Int): IsFavouriteCommand
}