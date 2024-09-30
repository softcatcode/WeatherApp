package com.softcat.weatherapp.presentation.hourly

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.softcat.weatherapp.domain.entity.Weather
import com.softcat.weatherapp.presentation.extensions.componentScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HourlyWeatherComponentImpl @AssistedInject constructor(
    storeFactory: HourlyWeatherStoreFactory,
    @Assisted("componentContext") componentContext: ComponentContext,
    @Assisted("hoursWeather") private val hoursWeather: List<Weather>,
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

    override fun back() {
        store.accept(HourlyWeatherStore.Intent.BackClick)
    }

    private fun labelCollector(label: HourlyWeatherStore.Label) = when (label) {
        HourlyWeatherStore.Label.BackClick -> onBackClick()
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("componentContext") componentContext: ComponentContext,
            @Assisted("hoursWeather") hoursWeather: List<Weather>,
            @Assisted("onBackClick") onBackClick: () -> Unit
        ): HourlyWeatherComponentImpl
    }
}