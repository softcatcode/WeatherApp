package com.softcat.weatherapp.presentation.profile

import android.content.Context
import android.net.Uri
import android.widget.Toast
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.softcat.database.exceptions.AvatarIsAbsentException
import com.softcat.domain.entity.User
import com.softcat.domain.entity.UserAvatar
import com.softcat.domain.useCases.AuthorizationUseCase
import com.softcat.domain.useCases.ClearWeatherDataUseCase
import com.softcat.domain.useCases.UserAvatarUseCase
import com.softcat.weatherapp.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class ProfileStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val clearWeatherUseCase: ClearWeatherDataUseCase,
    private val avatarUseCase: UserAvatarUseCase,
    private val authUseCase: AuthorizationUseCase
) {
    fun create(user: User): ProfileStore =
        object:
            ProfileStore, Store<ProfileStore.Intent, ProfileStore.State, ProfileStore.Label>
        by
            storeFactory.create(
                name = this::class.simpleName,
                initialState = ProfileStore.State(user),
                executorFactory = ::ProfileExecutor,
                reducer = ProfileReducer,
                bootstrapper = ProfileBootstrapper(user.id)
            ) {}

    private inner class ProfileBootstrapper(private val userId: String): CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch(Dispatchers.IO) {
                val result = avatarUseCase.get(userId)
                withContext(Dispatchers.Main) {
                    result.onSuccess {
                        dispatch(Action.AvatarLoaded(it))
                    }.onFailure {
                        if (it is AvatarIsAbsentException)
                            dispatch(Action.AvatarIsAbsent)
                        else
                            dispatch(Action.AvatarLoadingError(it.message ?: ""))
                    }
                }
            }
        }
    }

    private sealed interface Action {
        data class AvatarLoaded(val avatar: UserAvatar): Action

        data class AvatarLoadingError(val msg: String): Action

        data object AvatarIsAbsent: Action
    }

    private sealed interface Msg {
        data class AvatarLoaded(val avatar: UserAvatar): Msg

        data object AvatarLoading: Msg

        data object AvatarDeleted: Msg

        data object AvatarLoadingFinished: Msg

        data object ShowDialog: Msg

        data object DismissDialog: Msg
    }

    private inner class ProfileExecutor: CoroutineExecutor<ProfileStore.Intent, Action, ProfileStore.State, Msg, ProfileStore.Label>() {
        override fun executeIntent(
            intent: ProfileStore.Intent,
            getState: () -> ProfileStore.State
        ) {
            when (intent) {
                ProfileStore.Intent.BackClicked -> publish(ProfileStore.Label.BackClicked)
                ProfileStore.Intent.SettingsClicked -> publish(ProfileStore.Label.SettingsClicked)
                ProfileStore.Intent.ClearWeatherDataClicked -> {
                    scope.launch(Dispatchers.IO) {
                        clearWeatherUseCase()
                        withContext(Dispatchers.Main) {
                            publish(ProfileStore.Label.WeatherDataIsCleared)
                        }
                    }
                }
                is ProfileStore.Intent.SaveAvatar -> with (intent) {
                    getAndSaveAvatar(context, uri, userId)
                }

                ProfileStore.Intent.Exit -> {
                    scope.launch {
                        authUseCase.exit()
                        publish(ProfileStore.Label.Exited)
                    }
                }

                ProfileStore.Intent.ShowDialog -> dispatch(Msg.ShowDialog)

                ProfileStore.Intent.DismissDialog -> dispatch(Msg.DismissDialog)
            }
        }

        override fun executeAction(action: Action, getState: () -> ProfileStore.State) {
            when (action) {
                Action.AvatarIsAbsent -> dispatch(Msg.AvatarDeleted)

                is Action.AvatarLoaded -> dispatch(Msg.AvatarLoaded(action.avatar))

                is Action.AvatarLoadingError -> {
                    Timber.i("Avatar loading error: ${action.msg}")
                }
            }
        }

        fun getAndSaveAvatar(context: Context, uri: Uri?, userId: String) {
            dispatch(Msg.AvatarLoading)
            scope.launch(Dispatchers.IO) {
                avatarUseCase.read(context, uri).onSuccess { avatar ->
                    withContext(Dispatchers.Main) {
                        dispatch(Msg.AvatarLoaded(avatar))
                    }
                    avatarUseCase.save(userId, avatar).onSuccess {
                        showMessage(context, R.string.avatar_saved)
                    }.onFailure {
                        showMessage(context, R.string.avatar_save_error)
                    }
                }.onFailure {
                    showMessage(context, R.string.avatar_read_error)
                    withContext(Dispatchers.Main) {
                        dispatch(Msg.AvatarLoadingFinished)
                    }
                }
            }
        }

        fun showMessage(context: Context, msgId: Int) {
            scope.launch(Dispatchers.Main) {
                val text = context.getString(msgId)
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private object ProfileReducer: Reducer<ProfileStore.State, Msg> {
        override fun ProfileStore.State.reduce(msg: Msg): ProfileStore.State {
            val state = when (msg) {
                is Msg.AvatarLoaded ->
                    copy(avatarState = ProfileStore.State.AvatarState(msg.avatar))

                Msg.AvatarLoading -> copy(avatarState = avatarState.copy(updating = true))

                Msg.AvatarDeleted -> copy(avatarState = ProfileStore.State.AvatarState())

                Msg.AvatarLoadingFinished -> copy(avatarState = avatarState.copy(updating = false))

                Msg.ShowDialog -> copy(showDialog = true)

                Msg.DismissDialog -> copy(showDialog = false)
            }
            Timber.i("${this::class.simpleName} NEW_STATE: $state.")
            return state
        }
    }
}