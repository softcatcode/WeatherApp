package com.softcat.weatherapp.presentation.search

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

class SearchComponentImpl @Inject constructor(
    storeFactory: SearchStoreFactory,
    componentContext: ComponentContext,
    openReason: SearchOpenReason,
    private val onBackClickCallback: () -> Unit,
    private val onOpenForecastCallback: (City) -> Unit,
    private val onSavedToFavouritesCallback: () -> Unit
): SearchComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { storeFactory.create(openReason) }
    private val scope = componentScope()

    init {
        scope.launch {
            store.labels.collect(::labelCollector)
        }
    }

    private fun labelCollector(label: SearchStore.Label) = when (label) {
        SearchStore.Label.BackClick -> onBackClickCallback()
        is SearchStore.Label.OpenForecast -> onOpenForecastCallback(label.city)
        SearchStore.Label.SavedToFavourites -> onSavedToFavouritesCallback()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<SearchStore.State> = store.stateFlow

    override fun changeSearchQuery(query: String) {
        store.accept(SearchStore.Intent.ChangeSearchQuery(query))
    }

    override fun back() {
        store.accept(SearchStore.Intent.BackClick)
    }

    override fun clickSearch() {
        store.accept(SearchStore.Intent.SearchClick)
    }

    override fun clickCity(city: City) {
        store.accept(SearchStore.Intent.CityClick(city))
    }
}