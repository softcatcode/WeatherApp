package com.softcat.weatherapp.presentation.tech_interface

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.softcat.domain.useCases.AddToFavouriteUseCase
import com.softcat.domain.useCases.GetCurrentWeatherUseCase
import com.softcat.domain.useCases.GetFavouriteCitiesUseCase
import com.softcat.domain.useCases.GetForecastUseCase
import com.softcat.domain.useCases.RemoveFromFavouriteUseCase
import com.softcat.domain.useCases.SearchCityUseCase
import com.softcat.weatherapp.presentation.tech_interface.TechIntStoreFactory.Msg.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TechIntStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val searchUseCase: SearchCityUseCase,
    private val forecastUseCase: GetForecastUseCase,
    private val weatherUseCase: GetCurrentWeatherUseCase,
    private val addFavouriteUseCase: AddToFavouriteUseCase,
    private val removeFavouriteUseCase: RemoveFromFavouriteUseCase,
    private val getFavouriteUseCase: GetFavouriteCitiesUseCase,
) {

    fun create(): TechIntComponentStore = object:
        TechIntComponentStore,
        Store<TechIntComponentStore.Intent, TechIntComponentStore.State, TechIntComponentStore.Label>
    by
        storeFactory.create(
            name = this::class.simpleName,
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl(),
            initialState = TechIntComponentStore.State(0, "")
        ) {}

    private sealed interface Msg {
        data class AnswerIsReady(val data: String): Msg

        data class SelectUseCase(val index: Int): Msg
    }

    private inner class ExecutorImpl:
        CoroutineExecutor<
            TechIntComponentStore.Intent,
            Nothing,
            TechIntComponentStore.State,
            Msg,
            TechIntComponentStore.Label
        >() {

        override fun executeIntent(
            intent: TechIntComponentStore.Intent,
            getState: () -> TechIntComponentStore.State
        ) {
            when (intent) {
                is TechIntComponentStore.Intent.AddToFavourites -> {
                    scope.launch {
                        val city = searchUseCase(intent.cityName).getOrNull()?.firstOrNull()
                        city?.let {
                            addFavouriteUseCase(intent.userId, city)
                        }
                    }
                }
                TechIntComponentStore.Intent.BackClick -> {
                    publish(TechIntComponentStore.Label.BackClicked)
                }
                is TechIntComponentStore.Intent.GetCurrentWeather -> {
                    scope.launch {
                        val city = searchUseCase(intent.cityName).getOrNull()?.firstOrNull()
                        city?.let {
                            val result = weatherUseCase(city.id)
                            withContext(Dispatchers.Main) {
                                dispatch(AnswerIsReady(result.toString()))
                            }
                        }
                    }
                }
                is TechIntComponentStore.Intent.RemoveFromFavourites -> {
                    scope.launch { removeFavouriteUseCase(intent.userId, intent.cityId) }
                }
                is TechIntComponentStore.Intent.SelectUseCase -> {
                    dispatch(SelectUseCase(intent.index))
                }
                is TechIntComponentStore.Intent.GetFavouriteCities -> {
                    scope.launch {
                        val result = getFavouriteUseCase(intent.userId).first().toString()
                        withContext(Dispatchers.Main) {
                            dispatch(AnswerIsReady(result))
                        }
                    }
                }
                is TechIntComponentStore.Intent.GetForecast -> {
                    scope.launch {
                        val city = searchUseCase(intent.cityName).getOrNull()?.firstOrNull()
                        city?.let {
                            val result = forecastUseCase(city.id)
                            withContext(Dispatchers.Main) {
                                dispatch(AnswerIsReady(result.toString()))
                            }
                        }
                    }
                }

                is TechIntComponentStore.Intent.SearchUseCase -> {
                    scope.launch {
                        val result = searchUseCase(intent.query).toString()
                        withContext(Dispatchers.Main) {
                            dispatch(AnswerIsReady(result))
                        }
                    }
                }
            }
        }
    }

    private inner class ReducerImpl: Reducer<TechIntComponentStore.State, Msg> {
        override fun TechIntComponentStore.State.reduce(
            msg: Msg
        ): TechIntComponentStore.State {
            return when (msg) {
                is AnswerIsReady -> copy(answer = msg.data)
                is SelectUseCase -> copy(selectedUseCaseIndex = msg.index)
            }
        }
    }
}