package com.softcat.weatherapp.presentation.details

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.softcat.domain.entity.City
import com.softcat.domain.entity.Weather
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
    @Assisted("city") private val city: City,
    @Assisted("onBackClickCallback") private val onBackClickCallback: () -> Unit,
    @Assisted("openCityCalendarCallback") private val openCityCalendarCallback: () -> Unit,
    @Assisted("openHourlyWeatherCallback") private val openHourlyWeatherCallback: (List<Weather>) -> Unit
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
        DetailsStore.Label.OpenCityCalendarClicked -> openCityCalendarCallback()
        is DetailsStore.Label.OpenHourlyWeatherClicked -> {
            val state = model.value.forecastState as? DetailsStore.State.ForecastState.Loaded
            val hourlyWeather = state?.forecast?.hourly?.getOrNull(label.dayIndex).orEmpty()
            openHourlyWeatherCallback(hourlyWeather)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<DetailsStore.State> = store.stateFlow

    override fun changeFavouriteStatus() {
        store.accept(DetailsStore.Intent.ChangeFavouriteStatusClicked)
    }

    override fun back() {
        store.accept(DetailsStore.Intent.BackClicked)
    }

    override fun openCityCalendar() {
        store.accept(DetailsStore.Intent.OpenCityCalendarClicked)
    }

    override fun openHourlyWeather(dayIndex: Int) {
        store.accept(DetailsStore.Intent.OpenHourlyWeatherClicked(dayIndex))
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("componentContext") componentContext: ComponentContext,
            @Assisted("city") city: City,
            @Assisted("onBackClickCallback") onBackClickCallback: () -> Unit,
            @Assisted("openCityCalendarCallback") openCityCalendarCallback: () -> Unit,
            @Assisted("openHourlyWeatherCallback") openHourlyWeatherCallback: (List<Weather>) -> Unit
        ): DetailsComponentImpl
    }
}