package com.softcat.weatherapp.presentation.authorization

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.softcat.domain.entity.User
import com.softcat.domain.useCases.AuthorizationUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class AuthorizationStoreFactory @Inject constructor(
    private val authUseCase: AuthorizationUseCase,
    private val storeFactory: StoreFactory
) {
    fun create(): AuthorizationStore = object:
        AuthorizationStore,
        Store<AuthorizationStore.Intent, AuthorizationStore.State, AuthorizationStore.Label>
    by storeFactory.create(
        name = this::class.simpleName,
        initialState = AuthorizationStore.State(
            login = "",
            password = "",
            type = AuthorizationStore.State.ScreenType.Initial
        ),
        executorFactory = ::Executor,
        reducer = ReducerImpl,
        bootstrapper = BootstrapperImpl()
    ) {}

    private inner class BootstrapperImpl: CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch {
                val user = authUseCase.getLastUser()
                launchWeatherAppScreen(user)
            }
        }

        private fun launchWeatherAppScreen(user: User?) {
            if (user != null) {
                dispatch(Action.UserAlreadyAuthorized(user))
            } else {
                dispatch(Action.UserNotAuthorized)
            }
        }
    }

    private sealed interface Action {
        data class UserAlreadyAuthorized(val user: User): Action

        data object UserNotAuthorized: Action
    }

    sealed interface Msg {
        data class ChangePassword(val newValue: String): Msg
        data class ChangeRepeatedPassword(val newValue: String): Msg
        data class ChangeLogin(val newValue: String): Msg
        data class ChangeEmail(val newValue: String): Msg

        data object SwitchToSignIn: Msg
        data object SwitchToLogIn: Msg

        data class Error(val error: Throwable): Msg

        data object LoadingStarted: Msg
        data object LoadingFinished: Msg

        data object UserNotAuthorized: Msg
    }

    private inner class Executor: CoroutineExecutor<
        AuthorizationStore.Intent,
        Action,
        AuthorizationStore.State,
        Msg,
        AuthorizationStore.Label
    >() {
        override fun executeIntent(
            intent: AuthorizationStore.Intent,
            getState: () -> AuthorizationStore.State
        ) {
            Timber.i("${this::class.simpleName} INTENT $intent is caught.")
            when (intent) {
                is AuthorizationStore.Intent.Back ->
                    publish(AuthorizationStore.Label.BackClick)
                is AuthorizationStore.Intent.ChangeLogin ->
                    dispatch(Msg.ChangeLogin(intent.newValue))
                is AuthorizationStore.Intent.ChangeEmail ->
                    dispatch(Msg.ChangeEmail(intent.newValue))
                is AuthorizationStore.Intent.ChangePassword ->
                    dispatch(Msg.ChangePassword(intent.newValue))
                is AuthorizationStore.Intent.ChangeRepeatedPsw ->
                    dispatch(Msg.ChangeRepeatedPassword(intent.newValue))
                is AuthorizationStore.Intent.LogIn ->
                    logIn(intent.login, intent.password)
                is AuthorizationStore.Intent.SignIn ->
                    signIn(intent)
                AuthorizationStore.Intent.SwitchToLogIn ->
                    dispatch(Msg.SwitchToLogIn)
                AuthorizationStore.Intent.SwitchToSignIn ->
                    dispatch(Msg.SwitchToSignIn)
            }
        }

        override fun executeAction(action: Action, getState: () -> AuthorizationStore.State) {
            Timber.i("${this::class.simpleName} ACTION $action is caught.")
            when (action) {
                is Action.UserAlreadyAuthorized ->
                    publish(AuthorizationStore.Label.UserAlreadyAuthorized(action.user))

                Action.UserNotAuthorized ->
                    dispatch(Msg.UserNotAuthorized)
            }
        }

        private fun logIn(login: String, psw: String) {
            dispatch(Msg.LoadingStarted)
            scope.launch(Dispatchers.IO) {
                val result = authUseCase.logIn(login, psw)
                withContext(Dispatchers.Main) {
                    dispatch(Msg.LoadingFinished)
                    result.onSuccess {
                        Timber.i("Logged in by $login")
                        authUseCase.rememberUser(it)
                        publish(AuthorizationStore.Label.LoggedIn(it))
                    }.onFailure {
                        Timber.i("Log in rejected.")
                        dispatch(Msg.Error(it))
                    }
                }
            }
        }

        private fun signIn(intent: AuthorizationStore.Intent.SignIn) {
            val psw = intent.password
            val repeatedPsw = intent.repeatedPsw
            val email = intent.email
            val name = intent.login
            dispatch(Msg.LoadingStarted)
            if (repeatedPsw != psw) {
                dispatch(Msg.LoadingFinished)
                dispatch(Msg.Error(PasswordsNotMatchException()))
                return
            }
            scope.launch(Dispatchers.IO) {
                val result = authUseCase.signIn(name, email, psw)
                withContext(Dispatchers.Main) {
                    dispatch(Msg.LoadingFinished)
                    result.onSuccess {
                        Timber.i("Signed in by $name")
                        authUseCase.rememberUser(it)
                        publish(AuthorizationStore.Label.SignedIn(it))
                    }.onFailure {
                        Timber.i("Sign in rejected.")
                        dispatch(Msg.Error(it))
                    }
                }
            }
        }
    }

    private object ReducerImpl: Reducer<AuthorizationStore.State, Msg> {
        override fun AuthorizationStore.State.reduce(msg: Msg): AuthorizationStore.State {
            val result = when (msg) {
                is Msg.ChangeLogin -> copy(login = msg.newValue, error = null)
                is Msg.ChangePassword -> copy(password = msg.newValue, error = null)
                is Msg.ChangeRepeatedPassword -> copy(
                    type = (type as AuthorizationStore.State.ScreenType.SignIn)
                        .copy(repeatPassword = msg.newValue),
                    error = null
                )
                is Msg.ChangeEmail -> copy(
                    type = (type as AuthorizationStore.State.ScreenType.SignIn)
                        .copy(email = msg.newValue),
                    error = null
                )
                Msg.SwitchToLogIn -> copy(
                    type = AuthorizationStore.State.ScreenType.LogIn,
                    error = null
                )
                Msg.SwitchToSignIn -> copy(
                    type = AuthorizationStore.State.ScreenType.SignIn("", ""),
                    error = null
                )
                is Msg.Error -> copy(error = msg.error)
                Msg.LoadingStarted -> copy(isLoading = true, error = null)
                Msg.LoadingFinished -> copy(isLoading = false, error = null)
                Msg.UserNotAuthorized -> copy(type = AuthorizationStore.State.ScreenType.LogIn)
            }
            Timber.i("${this::class.simpleName} NEW_STATE: $result.")
            return result
        }
    }
}