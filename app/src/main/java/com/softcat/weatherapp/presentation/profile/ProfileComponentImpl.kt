package com.softcat.weatherapp.presentation.profile

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

class ProfileComponentImpl @AssistedInject constructor(
    storeFactory: ProfileStoreFactory,
    @Assisted("componentContext") componentContext: ComponentContext,
    @Assisted("user") user: User,
    @Assisted("back") private val backClickCallback: () -> Unit,
    @Assisted("settings") private val settingsClickCallback: () -> Unit
): ProfileComponent, ComponentContext by componentContext {
    private val store: ProfileStore = instanceKeeper.getStore { storeFactory.create(user) }
    private val scope = componentScope()

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<ProfileStore.State> = store.stateFlow

    init {
        scope.launch {
            store.labels.collect(::labelCollector)
        }
    }

    private fun labelCollector(label: ProfileStore.Label): Unit = when (label) {
        ProfileStore.Label.BackClicked -> backClickCallback()
        ProfileStore.Label.SettingsClicked -> settingsClickCallback()
    }

    override fun back() {
        Timber.i("${this::class.simpleName}.back()")
        store.accept(ProfileStore.Intent.BackClicked)
    }

    override fun openSettings() {
        Timber.i("${this::class.simpleName}.openSettings()")
        store.accept(ProfileStore.Intent.SettingsClicked)
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("user") user: User,
            @Assisted("componentContext") componentContext: ComponentContext,
            @Assisted("back") backClickCallback: () -> Unit,
            @Assisted("settings") settingsClickCallback: () -> Unit
        ): ProfileComponentImpl
    }
}