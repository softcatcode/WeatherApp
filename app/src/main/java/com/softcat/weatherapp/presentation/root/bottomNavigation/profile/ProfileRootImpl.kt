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
import com.softcat.weatherapp.presentation.root.bottomNavigation.profile.ProfileRootComponent.Child.*
import com.softcat.weatherapp.presentation.settings.SettingsComponentImpl
import com.softcat.weatherapp.presentation.web.WebComponentImpl
import com.softcat.weatherapp.presentation.tech_interface.TechIntComponentImpl
import com.softcat.domain.entity.WebPageType
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.parcelize.Parcelize
import timber.log.Timber

class ProfileRootImpl @AssistedInject constructor(
    private val profileComponentFactory: ProfileComponentImpl.Factory,
    private val settingsComponentFactory: SettingsComponentImpl.Factory,
    private val webComponentFactory: WebComponentImpl.Factory,
    private val techIntComponentFactory: TechIntComponentImpl.Factory,
    @Assisted("context") componentContext: ComponentContext,
    @Assisted("user") private val user: User
): ProfileRootComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, ProfileRootComponent.Child>> = childStack(
        source = navigation,
        initialConfiguration = Config.ProfileInfo(user),
        handleBackButton = true,
        childFactory = ::child
    )

    fun child(config: Config, componentContext: ComponentContext): ProfileRootComponent.Child {
        Timber.i("${this::class.simpleName}.child($config, $componentContext)")
        return when (config) {

            is Config.ProfileInfo -> {
                val component = profileComponentFactory.create(
                    user = config.user,
                    componentContext = componentContext,
                    backClickCallback = { navigation.pop() },
                    settingsClickCallback = { navigation.push(Config.Settings) }
                )
                Profile(component)
            }

            Config.Settings -> {
                val component = settingsComponentFactory.create(
                    componentContext = componentContext,
                    backClickCallback = { navigation.pop() },
                    openWebPageCallback = { navigation.push(Config.WebPage(it)) },
                    openTechInterfaceCallback = { navigation.push(Config.TechInterface) }
                )
                Settings(component)
            }

            is Config.WebPage -> {
                val component = webComponentFactory.create(
                    componentContext = componentContext,
                    onBackClick = { navigation.pop() },
                    pageType = config.type
                )
                WebPage(component)
            }

            is Config.TechInterface -> {
                val component = techIntComponentFactory.create(
                    componentContext = componentContext,
                    userId = user.id,
                    onBackClicked = { navigation.pop() }
                )
                TechInt(component)
            }
        }
    }

    sealed interface Config: Parcelable {

        @Parcelize
        data class ProfileInfo(val user: User): Config

        @Parcelize
        data object Settings: Config

        @Parcelize
        data class WebPage(
            val type: WebPageType
        ): Config

        @Parcelize
        data object TechInterface: Config
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("context") componentContext: ComponentContext,
            @Assisted("user") user: User,
        ): ProfileRootImpl
    }
}