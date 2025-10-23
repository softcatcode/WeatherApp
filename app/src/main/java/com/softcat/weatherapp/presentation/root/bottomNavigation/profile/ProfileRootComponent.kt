package com.softcat.weatherapp.presentation.root.bottomNavigation.profile

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.softcat.weatherapp.presentation.profile.ProfileComponent
import com.softcat.weatherapp.presentation.settings.SettingsComponent
import com.softcat.weatherapp.presentation.swagger.SwaggerComponent
import com.softcat.weatherapp.presentation.tech_interface.TechIntComponent

interface ProfileRootComponent {
    val stack: Value<ChildStack<*, Child>>

    sealed interface Child {
        data class Profile(val component: ProfileComponent): Child

        data class Settings(val component: SettingsComponent): Child

        data class SwaggerUI(val component: SwaggerComponent): Child

        data class TechInt(val component: TechIntComponent): Child
    }
}