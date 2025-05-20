package com.softcat.weatherapp.presentation.settings

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.softcat.weatherapp.presentation.extensions.componentScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class SettingsComponentImpl @AssistedInject constructor(
    private val storeFactory: SettingsStoreFactory,
    @Assisted("context") private val componentContext: ComponentContext,
    @Assisted("back") private val onBackClick: () -> Unit,
): SettingsComponent, ComponentContext by componentContext {

    private val store: SettingsStore = instanceKeeper.getStore { storeFactory.create() }
    private val scope = componentScope()

    init {
        scope.launch {
            store.labels.collect(::labelCollector)
        }
    }

    private fun labelCollector(label: SettingsStore.Label) {
        when (label) {
            SettingsStore.Label.BackClicked -> onBackClick()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<SettingsStore.State> = store.stateFlow

    override fun back() {
        Timber.i("${this::class.simpleName}.back()")
        store.accept(SettingsStore.Intent.Back)
    }

    override fun switchLab9Setting() {
        Timber.i("${this::class.simpleName}.switchLab9Setting()")
        store.accept(SettingsStore.Intent.SwitchLab9Setting)
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("context") componentContext: ComponentContext,
            @Assisted("back") onBackClick: () -> Unit,
        ): SettingsComponentImpl
    }
}