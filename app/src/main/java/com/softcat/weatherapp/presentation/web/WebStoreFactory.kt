package com.softcat.weatherapp.presentation.web

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import timber.log.Timber
import javax.inject.Inject

class WebStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
) {

    fun create(): WebStore =
        object:
            WebStore,
            Store<WebStore.Intent, WebStore.State, WebStore.Label>
        by
            storeFactory.create(
                name = this::class.simpleName,
                initialState = WebStore.State.Loading,
                executorFactory = ::Executor,
                reducer = ReducerImpl
            ) {}

    private sealed interface Action {
        data class HtmlContentLoaded(
            val data: String
        ): Action
    }

    private sealed interface Msg {
        data class Loaded(
            val data: String
        ): Msg
    }

    private inner class Executor: CoroutineExecutor<WebStore.Intent, Action, WebStore.State, Msg, WebStore.Label>() {
        override fun executeIntent(intent: WebStore.Intent, getState: () -> WebStore.State) {
            Timber.i("${this::class.simpleName} INTENT $intent is caught.")
            return when (intent) {
                WebStore.Intent.BackClick -> publish(WebStore.Label.BackClick)
            }
        }

        override fun executeAction(action: Action, getState: () -> WebStore.State) {
            Timber.i("${this::class.simpleName} ACTION $action is caught.")
            return when (action) {
                is Action.HtmlContentLoaded -> dispatch(Msg.Loaded(action.data))
            }
        }
    }

    private object ReducerImpl: Reducer<WebStore.State, Msg> {
        override fun WebStore.State.reduce(msg: Msg): WebStore.State {
            Timber.i("${this::class.simpleName} MSG $msg is caught.")
            return when (msg) {
                is Msg.Loaded -> WebStore.State.Content(msg.data)
            }
        }
    }
}