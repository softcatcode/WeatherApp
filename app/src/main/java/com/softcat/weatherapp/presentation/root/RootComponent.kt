package com.softcat.weatherapp.presentation.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.softcat.weatherapp.presentation.authorization.AuthorizationComponent
import com.softcat.weatherapp.presentation.root.bottomNavigation.BottomNavigationComponent

interface RootComponent {

    val stack: Value<ChildStack<*, Child>>
    
    sealed interface Child {

        data class Authorization(val component: AuthorizationComponent): Child

        data class Main(val component: BottomNavigationComponent): Child
    }
}