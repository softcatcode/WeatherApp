package com.softcat.weatherapp.presentation.root

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.jetpack.stack.Children
import com.softcat.weatherapp.presentation.details.DetailsContent
import com.softcat.weatherapp.presentation.favourite.FavouritesContent
import com.softcat.weatherapp.presentation.search.SearchContent
import com.softcat.weatherapp.presentation.ui.theme.WeatherAppTheme

@Composable
fun RootContent(component: RootComponent) {
    WeatherAppTheme {
        Children(stack = component.stack) {
            when (val instance = it.instance) {
                is RootComponent.Child.CityDetails -> DetailsContent(instance.component)
                is RootComponent.Child.Favourites -> FavouritesContent(instance.component)
                is RootComponent.Child.SearchCity -> SearchContent(instance.component)
            }
        }
    }
}