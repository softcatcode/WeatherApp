package com.softcat.weatherapp.presentation.calendar

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.softcat.domain.entity.City
import com.softcat.domain.entity.WeatherType
import com.softcat.weatherapp.presentation.extensions.componentScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class CalendarComponentImpl @AssistedInject constructor(
    private val storeFactory: CalendarStoreFactory,
    @Assisted("componentContext") val componentContext: ComponentContext,
    @Assisted("city") val city: City,
    @Assisted("onBackClicked") val onBackClicked: () -> Unit
): CalendarComponent, ComponentContext by componentContext {

    private val store: CalendarStore = instanceKeeper.getStore { storeFactory.create(city) }
    private val scope = componentScope()

    init {
        scope.launch {
            store.labels.collect(::labelCollector)
        }
    }

    private fun labelCollector(label: CalendarStore.Label) {
        Timber.i("${this::class.simpleName}: label $label collected.")
        when (label) {
            is CalendarStore.Label.BackClicked -> onBackClicked()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<CalendarStore.State> = store.stateFlow
    override fun back() {
        Timber.i("${this::class.simpleName}.back()")
        store.accept(CalendarStore.Intent.BackClicked)
    }

    override fun highlightDays() {
        Timber.i("${this::class.simpleName}.highlightDays()")
        store.accept(CalendarStore.Intent.LoadHighlightedDays)
    }

    override fun selectWeatherType(type: WeatherType) {
        Timber.i("${this::class.simpleName}.selectWeatherType($type)")
        store.accept(
            CalendarStore.Intent.ChangeWeatherType(type)
        )
    }

    override fun selectYear(year: Int) {
        Timber.i("${this::class.simpleName}.selectYear($year)")
        store.accept(
            CalendarStore.Intent.SelectYear(city, year)
        )
    }

    override fun changeMinTemp(temp: String) {
        Timber.i("${this::class.simpleName}.changeMinTemp($temp)")
        store.accept(
            CalendarStore.Intent.ChangeMinTemp(temp)
        )
    }

    override fun changeMaxTemp(temp: String) {
        Timber.i("${this::class.simpleName}.changeMaxTemp($temp)")
        store.accept(
            CalendarStore.Intent.ChangeMaxTemp(temp)
        )
    }

    override fun changeWindSpeed(newValue: ClosedFloatingPointRange<Float>) {
        Timber.i("${this::class.simpleName}.changeWindSpeed($newValue)")
        store.accept(
            CalendarStore.Intent.ChangeWindSpeed(newValue)
        )
    }

    override fun changeHumidity(newValue: ClosedFloatingPointRange<Float>) {
        Timber.i("${this::class.simpleName}.changeHumidity($newValue)")
        store.accept(
            CalendarStore.Intent.ChangeHumidity(newValue)
        )
    }

    override fun changePrecipitations(newValue: ClosedFloatingPointRange<Float>) {
        Timber.i("${this::class.simpleName}.changePrecipitations($newValue)")
        store.accept(
            CalendarStore.Intent.ChangePrecipitations(newValue)
        )
    }

    override fun changeSnowVolume(newValue: ClosedFloatingPointRange<Float>) {
        Timber.i("${this::class.simpleName}.changeSnowVolume($newValue)")
        store.accept(
            CalendarStore.Intent.ChangeSnowVolume(newValue)
        )
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("componentContext") componentContext: ComponentContext,
            @Assisted("city") city: City,
            @Assisted("onBackClicked") onBackClicked: () -> Unit,
        ): CalendarComponentImpl
    }
}