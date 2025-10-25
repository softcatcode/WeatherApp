package com.softcat.weatherapp.presentation.root.bottomNavigation.profile

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.jetpack.stack.Children
import com.softcat.weatherapp.presentation.profile.ProfileContent
import com.softcat.weatherapp.presentation.settings.SettingsScreen
import com.softcat.weatherapp.presentation.swagger.SwaggerScreen
import com.softcat.weatherapp.presentation.tech_interface.TechIntContent

@Composable
fun ProfileRootContent(component: ProfileRootComponent) {
    Children(component.stack) {
        when (val instance = it.instance) {
            is ProfileRootComponent.Child.Profile -> ProfileContent(instance.component)
            is ProfileRootComponent.Child.Settings -> SettingsScreen(instance.component)
            is ProfileRootComponent.Child.SwaggerUI -> SwaggerScreen(instance.component)
            is ProfileRootComponent.Child.TechInt -> TechIntContent(instance.component)
        }
    }
}