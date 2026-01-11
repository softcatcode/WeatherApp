package com.softcat.weatherapp.presentation.profile

import android.content.Context
import android.net.Uri
import com.arkivanov.mvikotlin.core.store.Store
import com.softcat.domain.entity.User
import com.softcat.domain.entity.UserAvatar

interface ProfileStore: Store<ProfileStore.Intent, ProfileStore.State, ProfileStore.Label> {

    sealed interface Intent {
        data object BackClicked: Intent

        data object SettingsClicked: Intent

        data object ClearWeatherDataClicked: Intent

        data class SaveAvatar(
            val context: Context,
            val uri: Uri?,
            val userId: String
        ): Intent

        data object Exit: Intent

        data object ShowDialog: Intent

        data object DismissDialog: Intent
    }

    data class State(
        val user: User,
        val avatarState: AvatarState = AvatarState(),
        val showDialog: Boolean = false
    ) {
        data class AvatarState(
            val avatar: UserAvatar? = null,
            val updating: Boolean = false,
        )
    }

    sealed interface Label {
        data object BackClicked: Label

        data object SettingsClicked: Label

        data object Exited: Label

        data object WeatherDataIsCleared: Label
    }
}