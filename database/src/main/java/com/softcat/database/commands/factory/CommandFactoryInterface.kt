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
import com.softcat.database.model.CityDbModel
import com.softcat.database.model.CountryDbModel
import com.softcat.database.model.CurrentWeatherDbModel
import com.softcat.database.model.WeatherDbModel
import com.softcat.database.model.WeatherTypeDbModel

interface CommandFactoryInterface {
    fun createUserCommand(name: String, email: String, password: String): CreateUserCommand

    fun getFavouriteCitiesCommand(userId: String): GetFavouriteCitiesCommand

    fun addCountryCommand(country: CountryDbModel): AddCountryCommand

    fun addCityCommand(city: CityDbModel): AddCityCommand

    fun getCountriesCommand(): GetCountriesCommand

    fun updateCountriesCommand(countries: List<CountryDbModel>): UpdateCountriesCommand

    fun verifyUserCommand(name: String, password: String): VerifyUserCommand

    fun addToFavouriteCommand(userId: String, cityId: Int): AddToFavouritesCommand

    fun removeFromFavouriteCommand(userId: String, cityId: Int): RemoveFromFavouritesCommand

    fun isFavouriteCommand(userId: String, cityId: Int): IsFavouriteCommand

    fun addWeatherCommand(model: WeatherDbModel): AddWeatherCommand

    fun addCurrentWeatherCommand(model: CurrentWeatherDbModel): AddCurrentWeatherCommand

    fun updateWeatherTypesCommand(types: List<WeatherTypeDbModel>): UpdateWeatherTypesCommand

    fun getCurrentWeatherCommand(cityId: Int, dayTimeEpoch: Long): GetCurrentWeatherCommand

    fun getDaysWeatherCommand(cityId: Int, startMillis: Long, endMillis: Long): GetDayWeatherCommand

    fun getWeatherTypesCommand(typeCodes: List<Int>): GetWeatherTypesCommand
}