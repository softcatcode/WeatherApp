package com.softcat.weatherapp.presentation.search

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.softcat.domain.entity.City
import com.softcat.domain.entity.User
import com.softcat.weatherapp.presentation.extensions.componentScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class SearchComponentImpl @AssistedInject constructor(
    storeFactory: SearchStoreFactory,
    @Assisted("user") private val user: User,
    @Assisted("componentContext") componentContext: ComponentContext,
    @Assisted("openReason") openReason: SearchOpenReason,
    @Assisted("onBackClickCallback") private val onBackClickCallback: () -> Unit,
    @Assisted("onOpenForecastCallback") private val onOpenForecastCallback: (User, City) -> Unit,
    @Assisted("onSavedToFavouritesCallback") private val onSavedToFavouritesCallback: () -> Unit
): SearchComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { storeFactory.create(user, openReason) }
    private val scope = componentScope()

    init {
        scope.launch {
            store.labels.collect(::labelCollector)
        }
    }

    private fun labelCollector(label: SearchStore.Label) {
        Timber.i("${this::class.simpleName}: label $label collected.")
        when (label) {
            SearchStore.Label.BackClick -> onBackClickCallback()
            is SearchStore.Label.OpenForecast -> onOpenForecastCallback(user, label.city)
            SearchStore.Label.SavedToFavourites -> onSavedToFavouritesCallback()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<SearchStore.State> = store.stateFlow

    override fun changeSearchQuery(query: String) {
        Timber.i("${this::class.simpleName}.changeSearchQuery($query)")
        store.accept(SearchStore.Intent.ChangeSearchQuery(query))
    }

    override fun back() {
        Timber.i("${this::class.simpleName}.back()")
        store.accept(SearchStore.Intent.BackClick)
    }

    override fun clickCity(city: City) {
        Timber.i("${this::class.simpleName}.clickCity($city)")
        store.accept(SearchStore.Intent.CityClick(city))
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("user") user: User,
            @Assisted("componentContext") componentContext: ComponentContext,
            @Assisted("openReason") openReason: SearchOpenReason,
            @Assisted("onBackClickCallback") onBackClickCallback: () -> Unit,
            @Assisted("onOpenForecastCallback") onOpenForecastCallback: (User, City) -> Unit,
            @Assisted("onSavedToFavouritesCallback") onSavedToFavouritesCallback: () -> Unit
        ): SearchComponentImpl
    }
}