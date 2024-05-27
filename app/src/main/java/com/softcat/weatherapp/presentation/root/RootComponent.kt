package com.softcat.weatherapp.presentation.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.softcat.weatherapp.presentation.details.DetailsComponent
import com.softcat.weatherapp.presentation.favourite.FavouritesComponent
import com.softcat.weatherapp.presentation.search.SearchComponent

interface RootComponent {

    val stack: Value<ChildStack<*, Child>>
    
    sealed interface Child {
        data class Favourites(val component: FavouritesComponent): Child

        data class SearchCity(val component: SearchComponent): Child

        data class CityDetails(val component: DetailsComponent): Child
    }
}