package com.softcat.weatherapp.presentation.swagger

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

class SwaggerComponentImpl @AssistedInject constructor(
    storeFactory: SwaggerStoreFactory,
    @Assisted("context") componentContext: ComponentContext,
    @Assisted("back") private val onBackClick: () -> Unit
): SwaggerComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { storeFactory.create() }
    private val scope = componentScope()

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<SwaggerStore.State> = store.stateFlow

    init {
        scope.launch {
            store.labels.collect(::labelCollector)
        }
    }

    private fun labelCollector(label: SwaggerStore.Label) {
        Timber.i("${this::class.simpleName}: label $label collected.")
        when (label) {
            SwaggerStore.Label.BackClick -> onBackClick()
        }
    }

    override fun back() {
        Timber.i("${this::class.simpleName}.back()")
        store.accept(SwaggerStore.Intent.BackClick)
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("context") componentContext: ComponentContext,
            @Assisted("back") onBackClick: () -> Unit
        ): SwaggerComponentImpl
    }
}