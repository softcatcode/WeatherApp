package com.softcat.weatherapp.presentation.web

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.softcat.domain.entity.WebPageType
import com.softcat.weatherapp.presentation.extensions.componentScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class WebComponentImpl @AssistedInject constructor(
    storeFactory: WebStoreFactory,
    @Assisted("context") componentContext: ComponentContext,
    @Assisted("back") private val onBackClick: () -> Unit,
    @Assisted("type") pageType: WebPageType
): WebComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { storeFactory.create(pageType) }
    private val scope = componentScope()

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<WebStore.State> = store.stateFlow

    init {
        scope.launch {
            store.labels.collect(::labelCollector)
        }
    }

    private fun labelCollector(label: WebStore.Label) {
        Timber.i("${this::class.simpleName}: label $label collected.")
        when (label) {
            WebStore.Label.BackClick -> onBackClick()
        }
    }

    override fun back() {
        Timber.i("${this::class.simpleName}.back()")
        store.accept(WebStore.Intent.BackClick)
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("context") componentContext: ComponentContext,
            @Assisted("back") onBackClick: () -> Unit,
            @Assisted("type") pageType: WebPageType
        ): WebComponentImpl
    }
}