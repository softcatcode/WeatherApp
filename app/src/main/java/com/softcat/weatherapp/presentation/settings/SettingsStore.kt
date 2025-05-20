package com.softcat.weatherapp.presentation.settings

import com.arkivanov.mvikotlin.core.store.Store

interface SettingsStore: Store<SettingsStore.Intent, SettingsStore.State, SettingsStore.Label> {

    sealed interface State {
        data class Settings(
            val localRegionStorage: Boolean
        ): State

        data object Initial: State
    }


    sealed interface Label {
        data object BackClicked: Label
    }

    sealed interface Intent {
        data object Back: Intent

        data object SwitchLab9Setting: Intent
    }
}