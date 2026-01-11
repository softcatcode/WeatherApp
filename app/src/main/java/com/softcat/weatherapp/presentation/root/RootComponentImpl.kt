package com.softcat.weatherapp.presentation.root

import android.os.Parcelable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.replaceCurrent
import com.arkivanov.decompose.value.Value
import com.softcat.domain.entity.User
import com.softcat.weatherapp.presentation.authorization.AuthorizationComponentImpl
import com.softcat.weatherapp.presentation.root.RootComponent.Child.*
import com.softcat.weatherapp.presentation.root.bottomNavigation.BottomNavigationComponentImpl
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.parcelize.Parcelize
import timber.log.Timber

class RootComponentImpl @AssistedInject constructor(
    private val authComponentFactory: AuthorizationComponentImpl.Factory,
    private val bottomNavigationFactory: BottomNavigationComponentImpl.Factory,
    @Assisted("componentContext") componentContext: ComponentContext,
): RootComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, RootComponent.Child>> = childStack(
        source = navigation,
        initialConfiguration = Config.Authorization,
        handleBackButton = true,
        childFactory = ::child
    )

    private fun child(config: Config, componentContext: ComponentContext): RootComponent.Child {
        Timber.i("${this::class.simpleName}.child($config, $componentContext)")
        val result = when (config) {

            Config.Authorization -> {
                val component = authComponentFactory.create(
                    componentContext = componentContext,
                    onBackClick = { navigation.pop() },
                    onSignIn = { user ->
                        navigation.replaceCurrent(Config.Main(user))
                    },
                    onLogIn = { user ->
                        navigation.replaceCurrent(Config.Main(user))
                    }
                )
                Authorization(component)
            }

            is Config.Main -> {
                val component = bottomNavigationFactory.create(
                    componentContext = componentContext,
                    user = config.user,
                    exitCallback = { navigation.replaceCurrent(Config.Authorization) }
                )
                Main(component)
            }
        }
        Timber.i("Result child: $result.")
        return result
    }

    sealed interface Config: Parcelable {
        @Parcelize
        data object Authorization: Config

        @Parcelize
        data class Main(val user: User): Config
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("componentContext") componentContext: ComponentContext
        ): RootComponentImpl
    }
}