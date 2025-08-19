package com.softcat.weatherapp.presentation.settings

import com.arkivanov.mvikotlin.core.store.Store

interface SettingsStore: Store<SettingsStore.Intent, SettingsStore.State, SettingsStore.Label> {

    sealed interface Intent {
        data object BackClick: Intent

        data object SendLogs: Intent
    }

    sealed interface State {
        data object Content: State
    }

    sealed interface Label {
        data object BackClick: Label
    }
}