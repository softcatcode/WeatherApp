package com.softcat.weatherapp.presentation.profile

import android.net.Uri
import com.arkivanov.mvikotlin.core.store.Store
import com.softcat.domain.entity.User

interface ProfileStore: Store<ProfileStore.Intent, ProfileStore.State, ProfileStore.Label> {

    sealed interface Intent {
        data object BackClicked: Intent

        data object SettingsClicked: Intent

        data object ClearWeatherDataClicked: Intent

        data class SaveAvatar(val uri: Uri?): Intent
    }

    sealed interface State {
        data class Content(val user: User): State
    }

    sealed interface Label {
        data object BackClicked: Label

        data object SettingsClicked: Label
    }
}