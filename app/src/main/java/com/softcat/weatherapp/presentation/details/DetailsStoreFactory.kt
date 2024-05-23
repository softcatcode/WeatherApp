package com.softcat.weatherapp.presentation.details

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor

internal class DetailsStoreFactory(
    private val storeFactory: StoreFactory
) {

    fun create(): DetailsStore =
        object: DetailsStore, Store<DetailsStore.Intent, DetailsStore.State, DetailsStore.Label>
        by storeFactory.create(
            name = this::class.simpleName,
            initialState = DetailsStore.State(Unit),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}


    private sealed interface Action {

    }

    private sealed interface Msg {

    }

    private class BootstrapperImpl: CoroutineBootstrapper<Action>() {
        override fun invoke() {
            TODO("Not yet implemented")
        }

    }

    private class ExecutorImpl:
        CoroutineExecutor<DetailsStore.Intent, Action, DetailsStore.State, Msg, DetailsStore.Label>() {

    }

    private object ReducerImpl: Reducer<DetailsStore.State, Msg> {
        override fun DetailsStore.State.reduce(msg: Msg): DetailsStore.State {
            TODO("Not yet implemented")
        }
    }
}