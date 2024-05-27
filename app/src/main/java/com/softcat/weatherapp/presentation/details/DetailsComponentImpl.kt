package com.softcat.weatherapp.presentation.details

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.softcat.weatherapp.domain.entity.City
import com.softcat.weatherapp.presentation.extensions.componentScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailsComponentImpl @AssistedInject constructor(
    storeFactory: DetailsStoreFactory,
    @Assisted("componentContext") componentContext: ComponentContext,
    @Assisted("city") city: City,
    @Assisted("onBackClickCallback") private val onBackClickCallback: () -> Unit
): DetailsComponent, ComponentContext by componentContext {

    private val store: DetailsStore = instanceKeeper.getStore { storeFactory.create(city) }
    private val scope = componentScope()

    init {
        scope.launch {
            store.labels.collect(::labelCollector)
        }
    }

    private fun labelCollector(label: DetailsStore.Label) = when (label) {
        DetailsStore.Label.BackClicked -> onBackClickCallback()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<DetailsStore.State> = store.stateFlow

    override fun changeFavouriteStatus() {
        store.accept(DetailsStore.Intent.ChangeFavouriteStatusClicked)
    }

    override fun back() {
        store.accept(DetailsStore.Intent.BackClicked)
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("componentContext") componentContext: ComponentContext,
            @Assisted("city") city: City,
            @Assisted("onBackClickCallback") onBackClickCallback: () -> Unit
        ): DetailsComponentImpl
    }
}