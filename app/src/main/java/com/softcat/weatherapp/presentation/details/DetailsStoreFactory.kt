package com.softcat.weatherapp.presentation.details

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.softcat.weatherapp.domain.entity.City
import com.softcat.weatherapp.domain.entity.Forecast
import com.softcat.weatherapp.domain.useCases.AddToFavouriteUseCase
import com.softcat.weatherapp.domain.useCases.GetForecastUseCase
import com.softcat.weatherapp.domain.useCases.ObserveIsFavouriteUseCase
import com.softcat.weatherapp.domain.useCases.RemoveFromFavouriteUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class DetailsStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val getForecastUseCase: GetForecastUseCase,
    private val observeIsFavouriteUseCase: ObserveIsFavouriteUseCase,
    private val addToFavouritesUseCase: AddToFavouriteUseCase,
    private val removeFromFavouritesUseCase: RemoveFromFavouriteUseCase,
) {

    fun create(city: City): DetailsStore =
        object: DetailsStore, Store<DetailsStore.Intent, DetailsStore.State, DetailsStore.Label>
        by storeFactory.create(
            name = this::class.simpleName,
            initialState = DetailsStore.State(
                city = city,
                isFavourite = false,
                forecastState = DetailsStore.State.ForecastState.Initial
            ),
            bootstrapper = BootstrapperImpl(city),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}


    private sealed interface Action {

        data class FavouriteStatusChanged(val isFavourite: Boolean): Action

        data class ForecastLoaded(val forecast: Forecast): Action

        data object ForecastLoadingStarted: Action

        data object ForecastLoadingError: Action
    }

    private sealed interface Msg {
        data class FavouriteStatusChanged(val isFavourite: Boolean): Msg

        data class ForecastLoaded(val forecast: Forecast): Msg

        data object LoadingError: Msg

        data object LoadingStarted: Msg
    }

    private inner class BootstrapperImpl(
        private val city: City
    ): CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch {
                observeIsFavouriteUseCase(city.id).collect {
                    dispatch(Action.FavouriteStatusChanged(it))
                }
            }
            scope.launch {
                try {
                    dispatch(Action.ForecastLoadingStarted)
                    val forecast = getForecastUseCase(city.id).let { forecast ->
                        forecast.copy(upcoming = forecast.upcoming)
                    }
                    dispatch(Action.ForecastLoaded(forecast))
                } catch (e: Exception) {
                    dispatch(Action.ForecastLoadingError)
                }
            }
        }
    }

    private inner class ExecutorImpl:
        CoroutineExecutor<DetailsStore.Intent, Action, DetailsStore.State, Msg, DetailsStore.Label>() {

        override fun executeAction(action: Action, getState: () -> DetailsStore.State) =
            when (action) {
                is Action.FavouriteStatusChanged -> dispatch(Msg.FavouriteStatusChanged(action.isFavourite))
                is Action.ForecastLoaded -> dispatch(Msg.ForecastLoaded(action.forecast))
                Action.ForecastLoadingError -> dispatch(Msg.LoadingError)
                Action.ForecastLoadingStarted -> dispatch(Msg.LoadingStarted)
            }

        override fun executeIntent(intent: DetailsStore.Intent, getState: () -> DetailsStore.State) {
            when (intent) {
                DetailsStore.Intent.BackClicked -> {
                    publish(DetailsStore.Label.BackClicked)
                }

                DetailsStore.Intent.ChangeFavouriteStatusClicked -> {
                    scope.launch {
                        val state = getState()
                        if (state.isFavourite)
                            removeFromFavouritesUseCase(state.city.id)
                        else
                            addToFavouritesUseCase(state.city)
                    }
                }

                DetailsStore.Intent.OpenCityCalendarClicked -> {
                    publish(DetailsStore.Label.OpenCityCalendarClicked)
                }

                is DetailsStore.Intent.OpenHourlyWeatherClicked -> {
                    publish(DetailsStore.Label.OpenHourlyWeatherClicked(intent.dayIndex))
                }
            }
        }
    }

    private object ReducerImpl: Reducer<DetailsStore.State, Msg> {
        override fun DetailsStore.State.reduce(msg: Msg): DetailsStore.State = when (msg) {

            is Msg.FavouriteStatusChanged -> copy(isFavourite = msg.isFavourite)

            is Msg.ForecastLoaded -> copy(forecastState = DetailsStore.State.ForecastState.Loaded(msg.forecast))

            Msg.LoadingError -> copy(forecastState = DetailsStore.State.ForecastState.Error)

            Msg.LoadingStarted -> copy(forecastState = DetailsStore.State.ForecastState.Loading)
        }
    }
}