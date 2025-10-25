package com.softcat.weatherapp.presentation.swagger

import com.arkivanov.mvikotlin.core.store.Store

interface SwaggerStore: Store<SwaggerStore.Intent, SwaggerStore.State, SwaggerStore.Label> {

    sealed interface Intent {
        data object BackClick: Intent
    }

    sealed interface State {
        data class Content(
            val swaggerLink: String
        ): State
    }

    sealed interface Label {
        data object BackClick: Label
    }
}