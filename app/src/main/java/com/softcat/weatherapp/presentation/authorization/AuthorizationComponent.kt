package com.softcat.weatherapp.presentation.authorization

import kotlinx.coroutines.flow.StateFlow

interface AuthorizationComponent {

    val model: StateFlow<AuthorizationStore.State>

    fun back()

    fun changeLogin(newValue: String)

    fun changeEmail(newValue: String)

    fun changePassword(newValue: String)

    fun changeRepeatedPassword(newValue: String)

    fun enter(login: String, password: String)

    fun register(
        login: String,
        email: String,
        psw: String,
        repeatedPsw: String
    )

    fun switchToSignIn()

    fun switchToLogIn()
}