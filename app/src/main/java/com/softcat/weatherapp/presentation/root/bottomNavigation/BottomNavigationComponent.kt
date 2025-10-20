package com.softcat.weatherapp.presentation.root.bottomNavigation

import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.router.pages.ChildPages
import com.arkivanov.decompose.value.Value
import com.softcat.weatherapp.presentation.root.bottomNavigation.profile.ProfileRootComponent
import com.softcat.weatherapp.presentation.root.bottomNavigation.weather.WeatherRootComponent

interface BottomNavigationComponent {

    fun selectPageByIndex(index: Int)

    @OptIn(ExperimentalDecomposeApi::class)
    val pages: Value<ChildPages<*, Child>>

    sealed interface Child {
        data class WeatherRoot(val component: WeatherRootComponent): Child

        data class ProfileRoot(val component: ProfileRootComponent): Child
    }
}