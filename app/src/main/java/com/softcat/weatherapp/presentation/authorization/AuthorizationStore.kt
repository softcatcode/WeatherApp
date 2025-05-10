package com.softcat.weatherapp.presentation.authorization

import com.arkivanov.mvikotlin.core.store.Store
import com.softcat.domain.entity.User

interface AuthorizationStore: Store<
    AuthorizationStore.Intent,
    AuthorizationStore.State,
    AuthorizationStore.Label
> {
    data class State(
        val login: String,
        val password: String,
        val type: ScreenType,
        val error: Throwable? = null,
        val isLoading: Boolean = false
    ) {
        sealed interface ScreenType {
            data object Initial: ScreenType

            data object LogIn: ScreenType

            data class SignIn(
                val email: String,
                val repeatPassword: String
            ): ScreenType
        }
    }

    sealed interface Label {
        data object BackClick: Label

        data class SignedIn(val user: User): Label
        data class LoggedIn(val user: User): Label
    }

    sealed interface Intent {
        data object Back: Intent

        data object SwitchToSignIn: Intent
        data object SwitchToLogIn: Intent

        data class ChangeLogin(val newValue: String): Intent
        data class ChangeEmail(val newValue: String): Intent
        data class ChangePassword(val newValue: String): Intent
        data class ChangeRepeatedPsw(val newValue: String): Intent

        data class LogIn(
            val login: String,
            val password: String
        ): Intent

        data class SignIn(
            val login: String,
            val email: String,
            val password: String,
            val repeatedPsw: String
        ): Intent
    }
}