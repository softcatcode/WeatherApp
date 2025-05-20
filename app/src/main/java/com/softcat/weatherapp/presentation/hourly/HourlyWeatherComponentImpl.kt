package com.softcat.weatherapp.presentation.hourly

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.softcat.domain.entity.CurrentWeather
import com.softcat.weatherapp.presentation.extensions.componentScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class HourlyWeatherComponentImpl @AssistedInject constructor(
    storeFactory: HourlyWeatherStoreFactory,
    @Assisted("componentContext") componentContext: ComponentContext,
    @Assisted("hoursWeather") private val hoursWeather: List<CurrentWeather>,
    @Assisted("onBackClick") private val onBackClick: () -> Unit
): HourlyWeatherComponent, ComponentContext by componentContext {

    private val store: HourlyWeatherStore = instanceKeeper.getStore {
        storeFactory.create(hoursWeather)
    }
    private val scope = componentScope()

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<HourlyWeatherStore.State> = store.stateFlow

    init {
        scope.launch {
            store.labels.collect(::labelCollector)
        }
    }

    private fun labelCollector(label: HourlyWeatherStore.Label) {
        Timber.i("${this::class.simpleName}: label $label collected.")
        when (label) {
            HourlyWeatherStore.Label.BackClick -> onBackClick()
        }
    }

    override fun back() {
        Timber.i("${this::class.simpleName}.back()")
        store.accept(HourlyWeatherStore.Intent.BackClick)
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("componentContext") componentContext: ComponentContext,
            @Assisted("hoursWeather") hoursWeather: List<CurrentWeather>,
            @Assisted("onBackClick") onBackClick: () -> Unit
        ): HourlyWeatherComponentImpl
    }
}