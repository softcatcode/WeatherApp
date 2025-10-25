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
import timber.log.Timber
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
                is TechIntComponentStore.Intent.AddToFavourites -> addToFavourites(intent.cityName, intent.userId)
                TechIntComponentStore.Intent.BackClick -> publish(TechIntComponentStore.Label.BackClicked)
                is TechIntComponentStore.Intent.GetCurrentWeather -> getHoursWeather(intent.userId, intent.cityName)
                is TechIntComponentStore.Intent.RemoveFromFavourites -> removeFromFavourites(intent.userId, intent.cityId)
                is TechIntComponentStore.Intent.SelectUseCase -> dispatch(SelectUseCase(intent.index))
                is TechIntComponentStore.Intent.GetFavouriteCities -> getFavourites(intent.userId)
                is TechIntComponentStore.Intent.GetForecast -> getForecast(intent.userId, intent.cityName)
                is TechIntComponentStore.Intent.SearchUseCase -> search(intent.userId, intent.query)
            }
        }

        private fun search(userId: String, query: String) {
            scope.launch {
                try {
                    val result = searchUseCase(userId, query).toString()
                    withContext(Dispatchers.Main) {
                        dispatch(AnswerIsReady(result))
                    }
                } catch (e: Exception) {
                    Timber.e(e)
                }
            }
        }

        private fun getForecast(userId: String,cityName: String) {
            scope.launch {
                try {
                    val city = searchUseCase(userId, cityName).getOrNull()?.firstOrNull()
                    city?.let {
                        val result = forecastUseCase(userId, city.id)
                        withContext(Dispatchers.Main) {
                            dispatch(AnswerIsReady(result.toString()))
                        }
                    }
                } catch (e: Exception) {
                    Timber.e(e)
                }
            }
        }

        private fun getHoursWeather(userId: String, cityName: String) {
            scope.launch {
                try {
                    val city = searchUseCase(userId, cityName).getOrNull()?.firstOrNull()
                    city?.let {
                        val result = weatherUseCase(userId, city.id)
                        withContext(Dispatchers.Main) {
                            dispatch(AnswerIsReady(result.toString()))
                        }
                    }
                } catch (e: Exception) {
                    Timber.e(e)
                }
            }
        }

        private fun addToFavourites(cityName: String, userId: String) {
            scope.launch {
                try {
                    val city = searchUseCase(userId, cityName).getOrNull()?.firstOrNull()
                    city?.let {
                        addFavouriteUseCase(userId, city)
                    }
                } catch (e: Exception) {
                    Timber.e(e)
                }
            }
        }

        private fun getFavourites(userId: String) {
            scope.launch {
                try {
                    val result = getFavouriteUseCase(userId).first().toString()
                    withContext(Dispatchers.Main) {
                        dispatch(AnswerIsReady(result))
                    }
                } catch (e: Exception) {
                    Timber.e(e)
                }
            }
        }

        private fun removeFromFavourites(userId: String, cityId: Int) {
            scope.launch {
                try {
                    removeFavouriteUseCase(userId, cityId)
                } catch (e: Exception) {
                    Timber.e(e)
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