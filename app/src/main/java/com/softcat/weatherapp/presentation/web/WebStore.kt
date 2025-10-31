package com.softcat.weatherapp.presentation.web

import com.arkivanov.mvikotlin.core.store.Store

interface WebStore: Store<WebStore.Intent, WebStore.State, WebStore.Label> {

    sealed interface Intent {
        data object BackClick: Intent
    }

    sealed interface State {
        data class Content(
            val link: String
        ): State
    }

    sealed interface Label {
        data object BackClick: Label
    }
}