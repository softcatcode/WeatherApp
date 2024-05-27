package com.softcat.weatherapp.presentation.favourite

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.softcat.weatherapp.domain.entity.City
import com.softcat.weatherapp.presentation.extensions.componentScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class FavouritesComponentImpl @Inject constructor(
    componentContext: ComponentContext,
    storeFactory: FavouritesStoreFactory,
    private val onCityItemClickedCallback: (City) -> Unit,
    private val onFavouriteClickCallback: () -> Unit,
    private val onSearchClickCallback: () -> Unit
): FavouritesComponent, ComponentContext by componentContext {

    private val store: FavouritesStore = instanceKeeper.getStore { storeFactory.create() }
    private val scope = componentScope()

    init {
        scope.launch {
            store.labels.collect(::labelCollector)
        }
    }

    private fun labelCollector(label: FavouritesStore.Label) = when (label) {
        FavouritesStore.Label.AddFavouritesClicked -> onFavouriteClickCallback()
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


}