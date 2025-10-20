package com.softcat.weatherapp.presentation.root.bottomNavigation.weather

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.jetpack.stack.Children
import com.softcat.weatherapp.presentation.calendar.CalendarContent
import com.softcat.weatherapp.presentation.details.DetailsContent
import com.softcat.weatherapp.presentation.favourite.FavouritesContent
import com.softcat.weatherapp.presentation.hourly.HourlyWeatherContent
import com.softcat.weatherapp.presentation.search.SearchContent

@Composable
fun WeatherRootContent(component: WeatherRootComponent) {
    Children(stack = component.stack) {
        when (val instance = it.instance) {
            is WeatherRootComponent.Child.Favourites -> FavouritesContent(instance.component)
            is WeatherRootComponent.Child.CityDetails -> DetailsContent(instance.component)
            is WeatherRootComponent.Child.SearchCity -> SearchContent(instance.component)
            is WeatherRootComponent.Child.Calendar -> CalendarContent(instance.component)
            is WeatherRootComponent.Child.HourlyWeather -> HourlyWeatherContent(instance.component)
        }
    }
}