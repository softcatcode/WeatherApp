package com.softcat.weatherapp.presentation.root.bottomNavigation.profile

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.jetpack.stack.Children
import com.softcat.weatherapp.presentation.profile.ProfileContent
import com.softcat.weatherapp.presentation.settings.SettingsScreen

@Composable
fun ProfileRootContent(component: ProfileRootComponent) {
    Children(component.stack) {
        when (val instance = it.instance) {
            is ProfileRootComponent.Child.Profile -> ProfileContent(instance.component)
            is ProfileRootComponent.Child.Settings -> SettingsScreen(instance.component)
        }
    }
}