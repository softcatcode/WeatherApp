package com.softcat.weatherapp.presentation.settings

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.softcat.domain.useCases.Lab9SettingsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class SettingsStoreFactory @Inject constructor(
    private val settingsUseCase: Lab9SettingsUseCase,
    private val storeFactory: StoreFactory
) {
    fun create(): SettingsStore = object:
        SettingsStore,
        Store<SettingsStore.Intent, SettingsStore.State, SettingsStore.Label>
    by storeFactory.create(
        name = this::class.simpleName,
        initialState = SettingsStore.State.Initial,
        executorFactory = ::Executor,
        reducer = ReducerImpl,
        bootstrapper = BootstrapperImpl()
    ) {}

    private inner class BootstrapperImpl: CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch {
                settingsUseCase.init()
                settingsUseCase.isLocalRegionStorage().collect {
                    dispatch(Action.RegionStorageMethodChanged(isLocalStorage = it))
                }
            }
        }
    }

    private sealed interface Action {
        data class RegionStorageMethodChanged(val isLocalStorage: Boolean): Action
    }

    sealed interface Msg {
        data class SwitchRegionStorageMethod(
            val isLocalStorage: Boolean
        ): Msg
    }

    private inner class Executor: CoroutineExecutor<
        SettingsStore.Intent,
        Action,
        SettingsStore.State,
        Msg,
        SettingsStore.Label
    >() {
        override fun executeIntent(
            intent: SettingsStore.Intent,
            getState: () -> SettingsStore.State
        ) {
            Timber.i("${this::class.simpleName} INTENT $intent is caught.")
            when (intent) {
                is SettingsStore.Intent.Back ->
                    publish(SettingsStore.Label.BackClicked)

                SettingsStore.Intent.SwitchLab9Setting -> {
                    scope.launch(Dispatchers.IO) {
                        settingsUseCase.switchRegionStorage()
                    }
                }
            }
        }

        override fun executeAction(action: Action, getState: () -> SettingsStore.State) {
            Timber.i("${this::class.simpleName} ACTION $action is caught.")
            when (action) {
                is Action.RegionStorageMethodChanged ->
                    dispatch(Msg.SwitchRegionStorageMethod(isLocalStorage = action.isLocalStorage))
            }
        }
    }

    private object ReducerImpl: Reducer<SettingsStore.State, Msg> {
        override fun SettingsStore.State.reduce(msg: Msg): SettingsStore.State {
            val result = when (msg) {
                is Msg.SwitchRegionStorageMethod -> {
                    SettingsStore.State.Settings(localRegionStorage = msg.isLocalStorage)
                }
            }
            Timber.i("${this::class.simpleName} NEW_STATE: $result.")
            return result
        }
    }
}