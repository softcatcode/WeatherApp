package com.softcat.weatherapp.presentation.details

import com.arkivanov.mvikotlin.core.store.Store

internal interface DetailsStore: Store<DetailsStore.Intent, DetailsStore.State, DetailsStore.Label> {

    sealed interface Intent {

    }

    data class State(
        val todo: Unit
    )

    sealed interface Label {

    }
}