package com.softcat.weatherapp.presentation.root.info

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.softcat.weatherapp.presentation.web.WebComponent

interface InfoRootComponent {

    val stack: Value<ChildStack<*, Child>>

    sealed interface Child {
        data class Web(val component: WebComponent): Child
    }
}