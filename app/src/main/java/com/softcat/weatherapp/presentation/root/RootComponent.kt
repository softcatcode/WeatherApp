package com.softcat.weatherapp.presentation.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.softcat.weatherapp.presentation.authorization.AuthorizationComponent
import com.softcat.weatherapp.presentation.calendar.CalendarComponent
import com.softcat.weatherapp.presentation.details.DetailsComponent
import com.softcat.weatherapp.presentation.favourite.FavouritesComponent
import com.softcat.weatherapp.presentation.hourly.HourlyWeatherComponent
import com.softcat.weatherapp.presentation.profile.ProfileComponent
import com.softcat.weatherapp.presentation.search.SearchComponent
import com.softcat.weatherapp.presentation.settings.SettingsComponent
import com.softcat.weatherapp.presentation.web.WebComponent

interface RootComponent {

    val stack: Value<ChildStack<*, Child>>

    enum class MainScreenSelection {
        Favourites,
        Profile
    }
    
    sealed interface Child {

        data class Favourites(val component: FavouritesComponent): Child

        data class SearchCity(val component: SearchComponent): Child

        data class CityDetails(val component: DetailsComponent): Child

        data class Calendar(val component: CalendarComponent): Child

        data class HourlyWeather(val component: HourlyWeatherComponent): Child

        data class Authorization(val component: AuthorizationComponent): Child

        data class Profile(val component: ProfileComponent): Child

        data class Settings(val component: SettingsComponent): Child

        data class Web(val component: WebComponent): Child
    }
}