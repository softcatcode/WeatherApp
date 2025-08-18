package com.softcat.weatherapp.presentation.favourite

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

class FavouritesComponentImpl @AssistedInject constructor(
    storeFactory: FavouritesStoreFactory,
    @Assisted("user") private val user: User,
    @Assisted("componentContext") componentContext: ComponentContext,
    @Assisted("onCityItemClickedCallback") private val onCityItemClickedCallback: (User, City) -> Unit,
    @Assisted("onAddToFavouritesClickCallback") private val onAddToFavouritesClickCallback: () -> Unit,
    @Assisted("onSearchClickCallback") private val onSearchClickCallback: () -> Unit,
    @Assisted("onProfileClickCallback") private val onProfileClickCallback: () -> Unit,
): FavouritesComponent, ComponentContext by componentContext {

    private val store: FavouritesStore = instanceKeeper.getStore { storeFactory.create(user) }
    private val scope = componentScope()

    init {
        scope.launch {
            store.labels.collect(::labelCollector)
        }
    }

    private fun labelCollector(label: FavouritesStore.Label) {
        Timber.i("${this::class.simpleName}: label $label collected.")
        when (label) {
            FavouritesStore.Label.AddFavouritesClicked -> onAddToFavouritesClickCallback()
            is FavouritesStore.Label.CityItemClicked -> onCityItemClickedCallback(user, label.city)
            FavouritesStore.Label.SearchClicked -> onSearchClickCallback()
            FavouritesStore.Label.ProfileClicked -> onProfileClickCallback()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<FavouritesStore.State> = store.stateFlow

    override fun onSearchClick() {
        Timber.i("${this::class.simpleName}.onSearchClick()")
        store.accept(FavouritesStore.Intent.SearchClicked)
    }

    override fun onProfileClick() {
        Timber.i("${this::class.simpleName}.onProfileClick()")
        store.accept(FavouritesStore.Intent.ProfileClicked)
    }

    override fun onAddToFavouritesClick() {
        Timber.i("${this::class.simpleName}.onAddToFavouritesClick()")
        store.accept(FavouritesStore.Intent.AddFavouritesClicked)
    }

    override fun onCityItemClick(city: City) {
        Timber.i("${this::class.simpleName}.onCityItemClick()")
        store.accept(FavouritesStore.Intent.CityItemClicked(city))
    }

    override fun reloadCities() {
        Timber.i("${this::class.simpleName}.reloadCities()")
        val cities = model.value.cityItems.map { it.city }
        store.accept(FavouritesStore.Intent.ReloadCities(cities))
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("user") user: User,
            @Assisted("componentContext") componentContext: ComponentContext,
            @Assisted("onCityItemClickedCallback") onCityItemClickedCallback: (User, City) -> Unit,
            @Assisted("onAddToFavouritesClickCallback") onAddToFavouritesClickCallback: () -> Unit,
            @Assisted("onSearchClickCallback") onSearchClickCallback: () -> Unit,
            @Assisted("onProfileClickCallback") onProfileClickCallback: () -> Unit,
        ): FavouritesComponentImpl
    }
}