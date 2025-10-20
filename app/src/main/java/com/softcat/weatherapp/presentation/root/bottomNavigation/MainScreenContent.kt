package com.softcat.weatherapp.presentation.root.bottomNavigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetpack.subscribeAsState
import com.softcat.weatherapp.presentation.root.bottomNavigation.profile.ProfileRootContent
import com.softcat.weatherapp.presentation.root.bottomNavigation.weather.WeatherRootContent
import com.softcat.weatherapp.presentation.utils.MainBottomBar

@OptIn(ExperimentalDecomposeApi::class)
@Composable
fun MainScreenContent(component: BottomNavigationComponent) {
    val pages = component.pages.subscribeAsState()
    val index = pages.value.selectedIndex
    val instance = pages.value.items[index].instance

    Scaffold(
        bottomBar = {
            MainBottomBar(
                currentIndex = index,
                onSelectIndex = { component.selectPageByIndex(it) }
            )
        }
    ) { paddings ->
        Box(
            modifier = Modifier.padding(paddings).fillMaxSize()
        ) {
            when (instance) {
                is BottomNavigationComponent.Child.WeatherRoot -> WeatherRootContent(instance.component)
                is BottomNavigationComponent.Child.ProfileRoot -> ProfileRootContent(instance.component)
                null -> {}
            }
        }
    }
}