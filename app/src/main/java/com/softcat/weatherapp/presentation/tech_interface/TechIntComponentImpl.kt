package com.softcat.weatherapp.presentation.tech_interface

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

class TechIntComponentImpl @AssistedInject constructor(
    @Assisted("context") componentContext: ComponentContext,
    @Assisted("userid") private val userId: String,
    @Assisted("back") private val onBackClicked: () -> Unit,
    private val storeFactory: TechIntStoreFactory,
): TechIntComponent, ComponentContext by componentContext {

    private val scope = componentScope()
    private val store = instanceKeeper.getStore { storeFactory.create() }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<TechIntComponentStore.State> = store.stateFlow

    init {
        scope.launch {
            store.labels.collect(::labelCollector)
        }
    }

    private fun labelCollector(label: TechIntComponentStore.Label) = when (label) {
        TechIntComponentStore.Label.BackClicked -> onBackClicked()
    }

    override fun search(query: String) {
        store.accept(TechIntComponentStore.Intent.SearchUseCase(userId, query))
    }

    override fun getCurrentWeather(cityName: String) {
        store.accept(TechIntComponentStore.Intent.GetCurrentWeather(userId, cityName))
    }

    override fun getForecast(cityName: String) {
        store.accept(TechIntComponentStore.Intent.GetForecast(userId, cityName))
    }

    override fun getFavouriteCities() {
        store.accept(TechIntComponentStore.Intent.GetFavouriteCities(userId))
    }

    override fun addToFavourites(cityName: String) {
        store.accept(TechIntComponentStore.Intent.AddToFavourites(userId, cityName))
    }

    override fun removeFromFavourites(cityId: Int) {
        store.accept(TechIntComponentStore.Intent.RemoveFromFavourites(userId, cityId))
    }

    override fun selectUseCase(index: Int) {
        store.accept(TechIntComponentStore.Intent.SelectUseCase(index))
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("context") componentContext: ComponentContext,
            @Assisted("userid") userId: String,
            @Assisted("back") onBackClicked: () -> Unit,
        ): TechIntComponentImpl
    }
}