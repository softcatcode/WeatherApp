package com.softcat.weatherapp.presentation.root.info

import android.os.Parcelable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.value.Value
import com.softcat.weatherapp.presentation.web.WebComponentImpl
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.parcelize.Parcelize
import timber.log.Timber

class InfoRootComponentImpl @AssistedInject constructor(
    private val webComponentFactory: WebComponentImpl.Factory,
    @Assisted("context") componentContext: ComponentContext
): InfoRootComponent, ComponentContext by componentContext {

    fun child(config: Config, componentContext: ComponentContext): InfoRootComponent.Child {
        Timber.i("${this::class.simpleName}.child($config, $componentContext)")
        return when (config) {
            Config.Web -> {
                val component = webComponentFactory.create(
                    componentContext = componentContext,
                    onBackClick = { navigation.pop() }
                )
                InfoRootComponent.Child.Web(component)
            }
        }
    }

    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, InfoRootComponent.Child>> = childStack(
        source = navigation,
        initialConfiguration = Config.Web,
        handleBackButton = true,
        childFactory = ::child
    )

    sealed interface Config: Parcelable {
        @Parcelize
        data object Web: Config
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("context") componentContext: ComponentContext
        ): InfoRootComponentImpl
    }
}