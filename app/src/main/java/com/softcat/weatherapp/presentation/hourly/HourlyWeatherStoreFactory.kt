package com.softcat.weatherapp.presentation.hourly

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.softcat.domain.entity.CurrentWeather
import timber.log.Timber
import javax.inject.Inject

class HourlyWeatherStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
) {
    fun create(hoursWeather: List<CurrentWeather>): HourlyWeatherStore =
        object:
            HourlyWeatherStore,
            Store<HourlyWeatherStore.Intent, HourlyWeatherStore.State, HourlyWeatherStore.Label>
        by
            storeFactory.create(
                name = this::class.simpleName,
                initialState = HourlyWeatherStore.State.Forecast(hoursWeather),
                executorFactory = ::ExecutorImpl,
                reducer = ReducerImpl
            ) {}

    private interface Action

    private interface Msg

    private inner class ExecutorImpl: CoroutineExecutor<HourlyWeatherStore.Intent, Action, HourlyWeatherStore.State, Msg, HourlyWeatherStore.Label>() {
        override fun executeIntent(
            intent: HourlyWeatherStore.Intent,
            getState: () -> HourlyWeatherStore.State
        ) {
            Timber.i("${this::class.simpleName} INTENT $intent is caught.")
            when (intent) {
                HourlyWeatherStore.Intent.BackClick -> publish(HourlyWeatherStore.Label.BackClick)
            }
        }
    }

    private object ReducerImpl: Reducer<HourlyWeatherStore.State, Msg> {
        override fun HourlyWeatherStore.State.reduce(msg: Msg): HourlyWeatherStore.State = this
    }
}