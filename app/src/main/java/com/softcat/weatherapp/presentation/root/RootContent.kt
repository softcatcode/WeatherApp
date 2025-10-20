package com.softcat.weatherapp.presentation.root

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.jetpack.stack.Children
import com.softcat.weatherapp.presentation.authorization.AuthorizationScreen
import com.softcat.weatherapp.presentation.root.bottomNavigation.MainScreenContent
import com.softcat.weatherapp.presentation.ui.theme.WeatherAppTheme

@Composable
fun RootContent(component: RootComponent) {
    WeatherAppTheme {
        Children(stack = component.stack) {
            when (val instance = it.instance) {
                is RootComponent.Child.Authorization -> AuthorizationScreen(instance.component)
                is RootComponent.Child.Main -> MainScreenContent(instance.component)
            }
        }
    }
}