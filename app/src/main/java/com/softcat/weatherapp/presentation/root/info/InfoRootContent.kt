package com.softcat.weatherapp.presentation.root.info

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.jetpack.stack.Children
import com.softcat.weatherapp.presentation.web.WebScreen

@Composable
fun InfoRootContent(component: InfoRootComponent) {
    Children(component.stack) {
        when (val instance = it.instance) {
            is InfoRootComponent.Child.Web -> WebScreen(instance.component)
        }
    }
}