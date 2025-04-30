package com.softcat.weatherapp.presentation.favourite

import android.content.Context
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.softcat.domain.entity.City
import com.softcat.domain.useCases.GetCurrentCityNameUseCase
import com.softcat.domain.useCases.GetCurrentWeatherUseCase
import com.softcat.domain.useCases.GetFavouriteCitiesUseCase
import com.softcat.domain.useCases.SaveToDatastoreUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class FavouritesStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase,
    private val getFavouriteCitiesUseCase: GetFavouriteCitiesUseCase,
    private val saveCityToDatastoreUseCase: SaveToDatastoreUseCase,
    private val getCurrentCityNameUseCase: GetCurrentCityNameUseCase,
    private val context: Context,
) {

    fun create(): FavouritesStore =
        object:
            FavouritesStore,
            Store<FavouritesStore.Intent, FavouritesStore.State, FavouritesStore.Label>
        by storeFactory.create(
            name = this::class.simpleName,
            initialState = FavouritesStore.State(cityItems = listOf()),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Msg {
        data class FavouriteCitiesLoaded(val cities: List<City>): Msg

        data class WeatherLoaded(
            val cityId: Int,
            val tempC: Float,
            val iconUrl: String
        ): Msg

        data class WeatherLoadingError(val cityId: Int): Msg

        data class CityIsLoading(val cityId: Int): Msg
    }

    private sealed interface Action {
        data class FavouriteCitiesLoaded(val cities: List<City>): Action
    }

    private inner class BootstrapperImpl: CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch {
                getCurrentCityNameUseCase(context) { cityName ->
                    CoroutineScope(Dispatchers.IO).launch {
                        saveCityToDatastoreUseCase(cityName)
                    }
                }
                getFavouriteCitiesUseCase().collect {
                    dispatch(Action.FavouriteCitiesLoaded(it))
                }
            }
        }
    }

    private object ReducerImpl: Reducer<FavouritesStore.State, Msg> {
        override fun FavouritesStore.State.reduce(msg: Msg) = when (msg) {
            is Msg.CityIsLoading -> {
                copy(
                    cityItems = cityItems.map {
                        if (it.city.id == msg.cityId) {
                            it.copy(weatherState = FavouritesStore.State.WeatherState.Loading)
                        } else it
                    }
                )
            }
            is Msg.FavouriteCitiesLoaded -> {
                copy(
                    cityItems = msg.cities.map {
                        FavouritesStore.State.CityItem(
                            city = it,
                            weatherState = FavouritesStore.State.WeatherState.Initial
                        )
                    }
                )
            }
            is Msg.WeatherLoaded -> {
                copy(
                    cityItems = cityItems.map {
                        if (it.city.id == msg.cityId) {
                            it.copy(
                                weatherState = FavouritesStore.State.WeatherState.Content(
                                    tempC = msg.tempC,
                                    iconUrl = msg.iconUrl
                                )
                            )
                        } else it
                    }
                )
            }
            is Msg.WeatherLoadingError -> {
                copy(
                    cityItems = cityItems.map {
                        if (it.city.id == msg.cityId) {
                            it.copy(weatherState = FavouritesStore.State.WeatherState.Error)
                        } else it
                    }
                )
            }
        }
    }

    private inner class ExecutorImpl:
        CoroutineExecutor<FavouritesStore.Intent, Action, FavouritesStore.State, Msg, FavouritesStore.Label>() {

        suspend fun loadCity(city: City) {
            try {
                dispatch(Msg.CityIsLoading(city.id))
                val weather = getCurrentWeatherUseCase(city.id)
                dispatch(
                    Msg.WeatherLoaded(
                        cityId = city.id,
                        tempC = weather.tempC,
                        iconUrl = weather.conditionUrl
                    )
                )
            } catch (e: Exception) {
                dispatch(Msg.WeatherLoadingError(city.id))
            }
        }

        fun loadCities(cities: List<City>) {
            cities.forEach {
                scope.launch { loadCity(it) }
            }
        }

        override fun executeAction(action: Action, getState: () -> FavouritesStore.State) =
            when(action) {
                is Action.FavouriteCitiesLoaded -> {
                    val cities = action.cities
                    dispatch(Msg.FavouriteCitiesLoaded(cities))
                    loadCities(action.cities)
                }
            }

        override fun executeIntent(
            intent: FavouritesStore.Intent,
            getState: () -> FavouritesStore.State
        ) = when (intent) {
            FavouritesStore.Intent.AddFavouritesClicked -> {
                publish(FavouritesStore.Label.AddFavouritesClicked)
            }
            is FavouritesStore.Intent.CityItemClicked -> {
                publish(FavouritesStore.Label.CityItemClicked(intent.city))
            }
            FavouritesStore.Intent.SearchClicked -> {
                publish(FavouritesStore.Label.SearchClicked)
            }
            is FavouritesStore.Intent.ReloadCities -> {
                loadCities(intent.cities)
            }
        }
    }

}