package com.softcat.weatherapp.presentation.search

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.softcat.weatherapp.domain.entity.City
import com.softcat.weatherapp.domain.useCases.AddToFavouriteUseCase
import com.softcat.weatherapp.domain.useCases.SearchCityUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val searchCityUseCase: SearchCityUseCase,
    private val addToFavouriteUseCase: AddToFavouriteUseCase

) {
    fun create(openReason: SearchOpenReason): SearchStore =
        object:
            SearchStore,
            Store<SearchStore.Intent, SearchStore.State, SearchStore.Label>
        by
            storeFactory.create(
                name = this::class.simpleName,
                initialState = SearchStore.State(
                    searchQuery = "",
                    searchState = SearchStore.State.SearchState.Initial
                ),
                executorFactory = { ExecutorImpl(openReason) },
                reducer = ReducerImpl
            ) {}

    private sealed interface Action

    private sealed interface Msg {
        data class ChangeSearchQuery(val query: String): Msg

        data class SearchResultLoaded(val cities: List<City>): Msg

        data object LoadingSearchResult: Msg

        data object SearchResultError: Msg
    }

    private inner class ExecutorImpl(private val openReason: SearchOpenReason):
        CoroutineExecutor<SearchStore.Intent, Action, SearchStore.State, Msg, SearchStore.Label>() {

        private var searchJob: Job? = null

        override fun executeIntent(intent: SearchStore.Intent, getState: () -> SearchStore.State) {
            when (intent) {
                SearchStore.Intent.BackClick -> publish(SearchStore.Label.BackClick)

                is SearchStore.Intent.ChangeSearchQuery -> {
                    dispatch(Msg.ChangeSearchQuery(intent.query))
                    launchSearchProcess(state = getState())
                }

                is SearchStore.Intent.CityClick -> {
                    when (openReason) {
                        SearchOpenReason.RegularSearch -> {
                            publish(SearchStore.Label.OpenForecast(intent.city))
                        }

                        SearchOpenReason.AddToFavourites -> {
                            scope.launch(Dispatchers.Main.immediate) {
                                addToFavouriteUseCase(intent.city)
                            }
                            publish(SearchStore.Label.SavedToFavourites)
                        }
                    }
                }
            }
        }

        private fun launchSearchProcess(state: SearchStore.State) {
            searchJob?.cancel()
            searchJob = scope.launch {
                try {
                    dispatch(Msg.LoadingSearchResult)
                    val cities = searchCityUseCase(state.searchQuery)
                    dispatch(Msg.SearchResultLoaded(cities))
                } catch (e: Exception) {
                    dispatch(Msg.SearchResultError)
                }
            }
        }
    }

    private object ReducerImpl: Reducer<SearchStore.State, Msg> {
        override fun SearchStore.State.reduce(msg: Msg) = when (msg) {

            is Msg.ChangeSearchQuery -> copy(searchQuery = msg.query)

            Msg.LoadingSearchResult -> copy(searchState = SearchStore.State.SearchState.Loading)

            Msg.SearchResultError -> copy(searchState = SearchStore.State.SearchState.Error)

            is Msg.SearchResultLoaded -> copy(
                searchState = SearchStore.State.SearchState.Success(msg.cities)
            )
        }
    }
}