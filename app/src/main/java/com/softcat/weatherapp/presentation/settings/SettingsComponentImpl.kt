package com.softcat.weatherapp.presentation.settings

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.softcat.weatherapp.presentation.extensions.componentScope
import com.softcat.domain.entity.WebPageType
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class SettingsComponentImpl @AssistedInject constructor(
    storeFactory: SettingsStoreFactory,
    @Assisted("context") componentContext: ComponentContext,
    @Assisted("back") private val backClickCallback: () -> Unit,
    @Assisted("web") private val openWebPageCallback: (WebPageType) -> Unit,
    @Assisted("tech") private val openTechInterfaceCallback: () -> Unit
): SettingsComponent, ComponentContext by componentContext {

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("context") componentContext: ComponentContext,
            @Assisted("back") backClickCallback: () -> Unit,
            @Assisted("web") openWebPageCallback: (WebPageType) -> Unit,
            @Assisted("tech") openTechInterfaceCallback: () -> Unit
        ): SettingsComponentImpl
    }

    private val scope = componentScope()
    private val store = instanceKeeper.getStore { storeFactory.create() }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<SettingsStore.State> = store.stateFlow

    init {
        scope.launch {
            store.labels.collect(::labelCollector)
        }
    }

    private fun labelCollector(label: SettingsStore.Label) {
        when (label) {
            SettingsStore.Label.BackClick -> backClickCallback()
            is SettingsStore.Label.OpenWebPageClicked -> openWebPageCallback(label.type)
            SettingsStore.Label.TechInterface -> openTechInterfaceCallback()
        }
    }

    override fun sendLogs() {
        Timber.i("${this::class.simpleName}.sendLogs()")
        store.accept(SettingsStore.Intent.SendLogs)
    }

    override fun openWebPage(type: WebPageType) {
        Timber.i("${this::class.simpleName}.openWebPage($type)")
        store.accept(SettingsStore.Intent.OpenWebPage(type))
    }

    override fun techInterface() {
        Timber.i("${this::class.simpleName}.techInterface()")
        store.accept(SettingsStore.Intent.TechInterface)
    }

    override fun back() {
        Timber.i("${this::class.simpleName}.back()")
        store.accept(SettingsStore.Intent.BackClick)
    }
}