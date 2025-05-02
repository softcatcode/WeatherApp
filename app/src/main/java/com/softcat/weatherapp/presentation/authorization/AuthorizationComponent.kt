package com.softcat.weatherapp.presentation.authorization

import kotlinx.coroutines.flow.StateFlow

interface AuthorizationComponent {

    val model: StateFlow<AuthorizationStore.State>

    fun back()

    fun changeLogin(newValue: String)

    fun changePassword(newValue: String)

    fun changeRepeatedPassword(newValue: String)

    fun enter(login: String, password: String)

    fun register(
        login: String,
        psw: String,
        repeatedPsw: String
    )

    fun switchToSignIn()

    fun switchToLogIn()
}