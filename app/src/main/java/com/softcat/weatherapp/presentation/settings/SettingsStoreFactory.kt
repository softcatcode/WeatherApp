package com.softcat.weatherapp.presentation.settings

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import javax.inject.Inject

class SettingsStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory
) {

    fun create(): SettingsStore =
        object:
            SettingsStore, Store<SettingsStore.Intent, SettingsStore.State, SettingsStore.Label>
        by
            storeFactory.create(
                name = this::class.simpleName,
                initialState = SettingsStore.State.Content,
                executorFactory = ::SettingsExecutor,
                reducer = SettingsReducer
            ) {}

    sealed interface Msg {}

    private inner class SettingsExecutor: CoroutineExecutor<SettingsStore.Intent, Nothing, SettingsStore.State, Msg, SettingsStore.Label>() {
        override fun executeIntent(
            intent: SettingsStore.Intent,
            getState: () -> SettingsStore.State
        ) {
            when (intent) {
                SettingsStore.Intent.BackClick -> publish(SettingsStore.Label.BackClick)
                SettingsStore.Intent.SendLogs -> {
                    // send logs logic
                }

                is SettingsStore.Intent.OpenWebPage -> {
                    publish(SettingsStore.Label.OpenWebPageClicked(intent.type))
                }

                SettingsStore.Intent.TechInterface -> {
                    publish(SettingsStore.Label.TechInterface)
                }
            }
        }
    }

    private object SettingsReducer: Reducer<SettingsStore.State, Msg> {
        override fun SettingsStore.State.reduce(msg: Msg): SettingsStore.State {
            return this
        }
    }
}