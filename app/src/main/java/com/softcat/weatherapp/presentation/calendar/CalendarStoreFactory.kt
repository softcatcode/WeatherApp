package com.softcat.weatherapp.presentation.calendar

import android.icu.util.Calendar
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.softcat.weatherapp.domain.entity.City
import com.softcat.weatherapp.domain.entity.WeatherParameters
import com.softcat.weatherapp.domain.entity.WeatherType
import com.softcat.weatherapp.domain.useCases.SelectYearDaysUseCase
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

class CalendarStoreFactory @Inject constructor(
    private val selectYearDaysUseCase: SelectYearDaysUseCase,
    val storeFactory: StoreFactory
) {
    fun create(city: City): CalendarStore =
        object: CalendarStore, Store<CalendarStore.Intent, CalendarStore.State, CalendarStore.Label>
        by storeFactory.create(
            name = this::class.simpleName,
            initialState = CalendarStore.State(
                year = Calendar.getInstance(Locale.getDefault())[Calendar.YEAR],
                weatherParams = WeatherParameters(),
                calendarState = CalendarStore.State.CalendarState.Initial,
                city = city
            ),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    sealed interface Action {
        data class CalendarUpdated(
            val highlightedDays: List<Set<Int>>
        ): Action

        data class Error(val error: Throwable): Action
    }

    sealed interface Msg {

        data object LoadingStarted: Msg

        data class Error(val error: Throwable): Msg

        data class HighlightDays(
            val days: List<Set<Int>>
        ): Msg

        data class ChangeWeatherType(
            val weatherType: WeatherType
        ): Msg

        data class SelectYear(
            val year: Int
        ): Msg

        data class ChangeMinTemp(
            val minTemp: String
        ): Msg

        data class ChangeMaxTemp(
            val maxTemp: String
        ): Msg

        data class ChangeHumidity(
            val humidity: ClosedFloatingPointRange<Float>
        ): Msg

        data class ChangePrecipitations(
            val precipitations: ClosedFloatingPointRange<Float>
        ): Msg

        data class ChangeWindSpeed(
            val windSpeed: ClosedFloatingPointRange<Float>
        ): Msg

        data class ChangeSnowVolume(
            val snowVolume: ClosedFloatingPointRange<Float>
        ): Msg
    }

    private inner class ExecutorImpl: CoroutineExecutor
        <CalendarStore.Intent, Action, CalendarStore.State, Msg, CalendarStore.Label>() {

        override fun executeAction(action: Action, getState: () -> CalendarStore.State) {
            when (action) {
                is Action.CalendarUpdated -> dispatch(Msg.HighlightDays(action.highlightedDays))
                is Action.Error -> dispatch(Msg.Error(action.error))
            }
        }

        override fun executeIntent(
            intent: CalendarStore.Intent,
            getState: () -> CalendarStore.State
        ) {
            when (intent) {
                is CalendarStore.Intent.ChangeHumidity ->
                    dispatch(Msg.ChangeHumidity(intent.humidity))
                is CalendarStore.Intent.ChangeMaxTemp ->
                    dispatch(Msg.ChangeMaxTemp(intent.maxTemp))
                is CalendarStore.Intent.ChangeMinTemp ->
                    dispatch(Msg.ChangeMinTemp(intent.minTemp))
                is CalendarStore.Intent.ChangePrecipitations ->
                    dispatch(Msg.ChangePrecipitations(intent.precipitations))
                is CalendarStore.Intent.ChangeSnowVolume ->
                    dispatch(Msg.ChangeSnowVolume(intent.snowVolume))
                is CalendarStore.Intent.ChangeWeatherType ->
                    dispatch(Msg.ChangeWeatherType(intent.weatherType))
                is CalendarStore.Intent.ChangeWindSpeed ->
                    dispatch(Msg.ChangeWindSpeed(intent.windSpeed))
                is CalendarStore.Intent.SelectYear -> {
                    val state = getState()
                    dispatch(Msg.LoadingStarted)
                    scope.launch {
                        selectYearDaysUseCase(state.weatherParams, state.city, intent.year)
                    }
                    dispatch(Msg.SelectYear(intent.year))
                }
                CalendarStore.Intent.BackClicked -> {
                    publish(CalendarStore.Label.BackClicked)
                }
                is CalendarStore.Intent.LoadHighlightedDays -> {
                    dispatch(Msg.LoadingStarted)
                    val state = getState()
                    scope.launch {
                        selectYearDaysUseCase(
                            state.weatherParams, state.city, state.year
                        ).onSuccess { data ->
                            dispatch(Msg.HighlightDays(data))
                        }.onFailure { throwable ->
                            dispatch(Msg.Error(throwable))
                        }
                    }
                }
            }
        }
    }

    private object ReducerImpl: Reducer<CalendarStore.State, Msg> {
        override fun CalendarStore.State.reduce(msg: Msg): CalendarStore.State = when (msg) {
            Msg.LoadingStarted ->
                copy(calendarState = CalendarStore.State.CalendarState.Loading)
            is Msg.ChangeHumidity ->
                copy(weatherParams = weatherParams.updateHumidity(msg.humidity))
            is Msg.ChangeMaxTemp ->
                copy(weatherParams = weatherParams.updateMaxTemperature(msg.maxTemp))
            is Msg.ChangeMinTemp ->
                copy(weatherParams = weatherParams.updateMinTemperature(msg.minTemp))
            is Msg.ChangePrecipitations ->
                copy(weatherParams = weatherParams.updatePrecipitations(msg.precipitations))
            is Msg.ChangeSnowVolume ->
                copy(weatherParams = weatherParams.updateSnowVolume(msg.snowVolume))
            is Msg.ChangeWindSpeed ->
                copy(weatherParams = weatherParams.updateWindSpeed(msg.windSpeed))
            is Msg.ChangeWeatherType ->
                copy(weatherParams = weatherParams.copy(weatherType = msg.weatherType))
            is Msg.SelectYear ->
                copy(year = msg.year)
            is Msg.HighlightDays -> {
                copy(calendarState = CalendarStore.State.CalendarState.Loaded(msg.days))
            }
            is Msg.Error ->
                copy(calendarState = CalendarStore.State.CalendarState.Error(msg.error))
        }
    }
}