package com.softcat.weatherapp.presentation.root.bottomNavigation.profile

import android.os.Parcelable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.softcat.domain.entity.User
import com.softcat.weatherapp.presentation.profile.ProfileComponentImpl
import com.softcat.weatherapp.presentation.settings.SettingsComponentImpl
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.parcelize.Parcelize
import timber.log.Timber

class ProfileRootImpl @AssistedInject constructor(
    private val profileComponentFactory: ProfileComponentImpl.Factory,
    private val settingsComponentFactory: SettingsComponentImpl.Factory,
    @Assisted("context") componentContext: ComponentContext,
    @Assisted("user") user: User
): ProfileRootComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, ProfileRootComponent.Child>> = childStack(
        source = navigation,
        initialConfiguration = Config.Profile(user),
        handleBackButton = true,
        childFactory = ::child
    )

    fun child(config: Config, componentContext: ComponentContext): ProfileRootComponent.Child {
        Timber.i("${this::class.simpleName}.child($config, $componentContext)")
        return when (config) {

            is Config.Profile -> {
                val component = profileComponentFactory.create(
                    user = config.user,
                    componentContext = componentContext,
                    backClickCallback = { navigation.pop() },
                    settingsClickCallback = { navigation.push(Config.Settings) }
                )
                ProfileRootComponent.Child.Profile(component)
            }

            Config.Settings -> {
                val component = settingsComponentFactory.create(
                    componentContext = componentContext,
                    backClickCallback = { navigation.pop() }
                )
                ProfileRootComponent.Child.Settings(component)
            }
        }
    }

    sealed interface Config: Parcelable {

        @Parcelize
        data class Profile(val user: User): Config

        @Parcelize
        data object Settings: Config
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("context") componentContext: ComponentContext,
            @Assisted("user") user: User
        ): ProfileRootImpl
    }
}