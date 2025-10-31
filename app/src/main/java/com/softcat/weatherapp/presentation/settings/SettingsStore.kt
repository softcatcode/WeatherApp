package com.softcat.weatherapp.presentation.settings

import com.arkivanov.mvikotlin.core.store.Store
import com.softcat.domain.entity.WebPageType

interface SettingsStore: Store<SettingsStore.Intent, SettingsStore.State, SettingsStore.Label> {

    sealed interface Intent {
        data object BackClick: Intent

        data object SendLogs: Intent

        data class OpenWebPage(val type: WebPageType): Intent

        data object TechInterface: Intent
    }

    sealed interface State {
        data object Content: State
    }

    sealed interface Label {
        data object BackClick: Label

        data class OpenWebPageClicked(val type: WebPageType): Label

        data object TechInterface: Label
    }
}