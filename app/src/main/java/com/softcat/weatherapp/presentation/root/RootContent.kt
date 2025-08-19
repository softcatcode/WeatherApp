package com.softcat.weatherapp.presentation.root

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.jetpack.stack.Children
import com.softcat.weatherapp.presentation.authorization.AuthorizationScreen
import com.softcat.weatherapp.presentation.calendar.CalendarContent
import com.softcat.weatherapp.presentation.details.DetailsContent
import com.softcat.weatherapp.presentation.favourite.FavouritesContent
import com.softcat.weatherapp.presentation.hourly.HourlyWeatherContent
import com.softcat.weatherapp.presentation.profile.ProfileContent
import com.softcat.weatherapp.presentation.search.SearchContent
import com.softcat.weatherapp.presentation.settings.SettingsScreen
import com.softcat.weatherapp.presentation.ui.theme.WeatherAppTheme

@Composable
fun RootContent(component: RootComponent) {
    WeatherAppTheme {
        Children(stack = component.stack) {
            when (val instance = it.instance) {
                is RootComponent.Child.Favourites -> FavouritesContent(instance.component)
                is RootComponent.Child.Profile -> ProfileContent(instance.component)
                is RootComponent.Child.CityDetails -> DetailsContent(instance.component)
                is RootComponent.Child.SearchCity -> SearchContent(instance.component)
                is RootComponent.Child.Calendar -> CalendarContent(instance.component)
                is RootComponent.Child.HourlyWeather -> HourlyWeatherContent(instance.component)
                is RootComponent.Child.Authorization -> AuthorizationScreen(instance.component)
                is RootComponent.Child.Settings -> SettingsScreen(instance.component)
            }
        }
    }
}