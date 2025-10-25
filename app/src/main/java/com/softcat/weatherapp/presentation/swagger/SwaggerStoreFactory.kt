package com.softcat.weatherapp.presentation.swagger

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.softcat.domain.useCases.GetSwaggerUIUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class SwaggerStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val swaggerUseCase: GetSwaggerUIUseCase
) {

    fun create(): SwaggerStore =
        object:
            SwaggerStore,
            Store<SwaggerStore.Intent, SwaggerStore.State, SwaggerStore.Label>
        by
            storeFactory.create(
                name = this::class.simpleName,
                initialState = SwaggerStore.State.Content(swaggerUseCase.getSwaggerLink()),
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

    private inner class Executor: CoroutineExecutor<SwaggerStore.Intent, Action, SwaggerStore.State, Msg, SwaggerStore.Label>() {
        override fun executeIntent(intent: SwaggerStore.Intent, getState: () -> SwaggerStore.State) {
            Timber.i("${this::class.simpleName} INTENT $intent is caught.")
            return when (intent) {
                SwaggerStore.Intent.BackClick -> publish(SwaggerStore.Label.BackClick)
            }
        }

        override fun executeAction(action: Action, getState: () -> SwaggerStore.State) {
            Timber.i("${this::class.simpleName} ACTION $action is caught.")
            return when (action) {
                is Action.HtmlContentLoaded -> dispatch(Msg.Loaded(action.data))
            }
        }
    }

    private object ReducerImpl: Reducer<SwaggerStore.State, Msg> {
        override fun SwaggerStore.State.reduce(msg: Msg): SwaggerStore.State {
            Timber.i("${this::class.simpleName} MSG $msg is caught.")
            return when (msg) {
                is Msg.Loaded -> SwaggerStore.State.Content(msg.data)
            }
        }
    }
}