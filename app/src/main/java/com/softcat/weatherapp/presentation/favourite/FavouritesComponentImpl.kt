package com.softcat.weatherapp.presentation.favourite

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

class FavouritesComponentImpl @AssistedInject constructor(
    storeFactory: FavouritesStoreFactory,
    @Assisted("componentContext") componentContext: ComponentContext,
    @Assisted("onCityItemClickedCallback") private val onCityItemClickedCallback: (City) -> Unit,
    @Assisted("onAddToFavouritesClickCallback") private val onAddToFavouritesClickCallback: () -> Unit,
    @Assisted("onSearchClickCallback") private val onSearchClickCallback: () -> Unit
): FavouritesComponent, ComponentContext by componentContext {

    private val store: FavouritesStore = instanceKeeper.getStore { storeFactory.create() }
    private val scope = componentScope()

    init {
        scope.launch {
            store.labels.collect(::labelCollector)
        }
    }

    private fun labelCollector(label: FavouritesStore.Label) = when (label) {
        FavouritesStore.Label.AddFavouritesClicked -> onAddToFavouritesClickCallback()
        is FavouritesStore.Label.CityItemClicked -> onCityItemClickedCallback(label.city)
        FavouritesStore.Label.SearchClicked -> onSearchClickCallback()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<FavouritesStore.State> = store.stateFlow

    override fun onSearchClick() {
        store.accept(FavouritesStore.Intent.SearchClicked)
    }

    override fun onAddToFavouritesClick() {
        store.accept(FavouritesStore.Intent.AddFavouritesClicked)
    }

    override fun onCityItemClick(city: City) {
        store.accept(FavouritesStore.Intent.CityItemClicked(city))
    }

    override fun reloadCities() {
        val cities = model.value.cityItems.map { it.city }
        store.accept(FavouritesStore.Intent.ReloadCities(cities))
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("componentContext") componentContext: ComponentContext,
            @Assisted("onCityItemClickedCallback") onCityItemClickedCallback: (City) -> Unit,
            @Assisted("onAddToFavouritesClickCallback") onAddToFavouritesClickCallback: () -> Unit,
            @Assisted("onSearchClickCallback") onSearchClickCallback: () -> Unit
        ): FavouritesComponentImpl
    }
}