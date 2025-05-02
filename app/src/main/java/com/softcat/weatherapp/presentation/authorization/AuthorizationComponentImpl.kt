package com.softcat.weatherapp.presentation.authorization

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.softcat.domain.entity.User
import com.softcat.weatherapp.presentation.extensions.componentScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class AuthorizationComponentImpl @AssistedInject constructor(
    private val storeFactory: AuthorizationStoreFactory,
    @Assisted("context") private val componentContext: ComponentContext,
    @Assisted("back") private val onBackClick: () -> Unit,
    @Assisted("signIn") private val onSignIn: (User) -> Unit,
    @Assisted("logIn") private val onLogIn: (User) -> Unit,
): AuthorizationComponent, ComponentContext by componentContext {

    private val store: AuthorizationStore = instanceKeeper.getStore { storeFactory.create() }
    private val scope = componentScope()

    init {
        scope.launch {
            store.labels.collect(::labelCollector)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<AuthorizationStore.State> = store.stateFlow

    private fun labelCollector(label: AuthorizationStore.Label) {
        Timber.i("${this::class.simpleName}: label $label collected.")
        when (label) {
            AuthorizationStore.Label.BackClick -> onBackClick()
            is AuthorizationStore.Label.LoggedIn -> onLogIn(label.user)
            is AuthorizationStore.Label.SignedIn -> onSignIn(label.user)
        }
    }

    override fun back() {
        Timber.i("${this::class.simpleName}.back()")
        store.accept(AuthorizationStore.Intent.Back)
    }

    override fun changeLogin(newValue: String) {
        Timber.i("${this::class.simpleName}.changeLogin()")
        store.accept(AuthorizationStore.Intent.ChangeLogin(newValue))
    }

    override fun changePassword(newValue: String) {
        Timber.i("${this::class.simpleName}.changePassword()")
        store.accept(AuthorizationStore.Intent.ChangePassword(newValue))
    }

    override fun changeRepeatedPassword(newValue: String) {
        Timber.i("${this::class.simpleName}.changeRepeatedPassword()")
        store.accept(AuthorizationStore.Intent.ChangeRepeatedPsw(newValue))
    }

    override fun enter(login: String, password: String) {
        Timber.i("${this::class.simpleName}.enter()")
        store.accept(AuthorizationStore.Intent.LogIn(login, password))
    }

    private fun formatName(name: String): String {
        val sb = StringBuilder()
        var i = 0
        while (i < name.length) {
            if (name[i] == ' ') {
                ++i
                continue
            }
            var j = i + 1
            while (j < name.length && name[j] != ' ')
                ++j
            sb.append(name.substring(i, j))
            sb.append(' ')
            i = j
        }
        sb.deleteCharAt(sb.lastIndex)
        return sb.toString()
    }

    override fun register(login: String, psw: String, repeatedPsw: String) {
        Timber.i("${this::class.simpleName}.register()")
        store.accept(AuthorizationStore.Intent.SignIn(formatName(login), psw, repeatedPsw))
    }

    override fun switchToSignIn() {
        Timber.i("${this::class.simpleName}.switchToSignIn()")
        store.accept(AuthorizationStore.Intent.SwitchToSignIn)
    }

    override fun switchToLogIn() {
        Timber.i("${this::class.simpleName}.switchToLogIn()")
        store.accept(AuthorizationStore.Intent.SwitchToLogIn)
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("context") componentContext: ComponentContext,
            @Assisted("back") onBackClick: () -> Unit,
            @Assisted("signIn") onSignIn: (User) -> Unit,
            @Assisted("logIn") onLogIn: (User) -> Unit,
        ): AuthorizationComponentImpl
    }
}