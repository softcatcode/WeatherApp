package com.softcat.weatherapp.presentation.profile

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.softcat.domain.entity.User
import javax.inject.Inject

class ProfileStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory
) {
    fun create(user: User): ProfileStore =
        object:
            ProfileStore, Store<ProfileStore.Intent, ProfileStore.State, ProfileStore.Label>
        by
            storeFactory.create(
                name = this::class.simpleName,
                initialState = ProfileStore.State.Content(user),
                executorFactory = ::ProfileExecutor,
                reducer = ProfileReducer
            ) {}

    private sealed interface Msg {}

    private class ProfileExecutor: CoroutineExecutor<ProfileStore.Intent, Nothing, ProfileStore.State, Msg, ProfileStore.Label>() {
        override fun executeIntent(
            intent: ProfileStore.Intent,
            getState: () -> ProfileStore.State
        ) {
            when (intent) {
                ProfileStore.Intent.BackClicked -> publish(ProfileStore.Label.BackClicked)
                ProfileStore.Intent.SettingsClicked -> publish(ProfileStore.Label.SettingsClicked)
            }
        }
    }

    private object ProfileReducer: Reducer<ProfileStore.State, Msg> {
        override fun ProfileStore.State.reduce(msg: Msg): ProfileStore.State {
            return this
        }
    }
}