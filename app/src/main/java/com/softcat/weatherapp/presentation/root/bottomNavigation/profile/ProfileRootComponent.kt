package com.softcat.weatherapp.presentation.root.bottomNavigation.profile

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.softcat.weatherapp.presentation.profile.ProfileComponent
import com.softcat.weatherapp.presentation.settings.SettingsComponent

interface ProfileRootComponent {
    val stack: Value<ChildStack<*, Child>>

    sealed interface Child {
        data class Profile(val component: ProfileComponent): Child

        data class Settings(val component: SettingsComponent): Child
    }
}