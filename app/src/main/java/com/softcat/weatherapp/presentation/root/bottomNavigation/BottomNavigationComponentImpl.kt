package com.softcat.weatherapp.presentation.root.bottomNavigation

import android.os.Parcelable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.router.pages.ChildPages
import com.arkivanov.decompose.router.pages.Pages
import com.arkivanov.decompose.router.pages.PagesNavigation
import com.arkivanov.decompose.router.pages.childPages
import com.arkivanov.decompose.router.pages.select
import com.arkivanov.decompose.value.Value
import com.softcat.domain.entity.User
import com.softcat.weatherapp.presentation.root.bottomNavigation.profile.ProfileRootImpl
import com.softcat.weatherapp.presentation.root.bottomNavigation.weather.WeatherRootImpl
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.parcelize.Parcelize
import timber.log.Timber

class BottomNavigationComponentImpl @AssistedInject constructor(
    @Assisted("context") componentContext: ComponentContext,
    @Assisted("user") private val user: User,
    private val weatherRootFactory: WeatherRootImpl.Factory,
    private val profileRootFactory: ProfileRootImpl.Factory
): BottomNavigationComponent, ComponentContext by componentContext {

//    private val weatherRoot = instanceKeeper.getOrCreateSimple<WeatherRootComponent> {
//        weatherRootFactory.create(
//            componentContext = componentContext,
//            user = user
//        )
//    }
//    private val profileRoot = instanceKeeper.getOrCreateSimple<ProfileRootComponent> {
//        profileRootFactory.create(
//            componentContext = componentContext,
//            user = user
//        )
//    }

    @OptIn(ExperimentalDecomposeApi::class)
    val navigation = PagesNavigation<Config>()

    @OptIn(ExperimentalDecomposeApi::class)
    override fun selectPageByIndex(index: Int) {
        navigation.select(index)
    }

    @OptIn(ExperimentalDecomposeApi::class)
    override val pages: Value<ChildPages<*, BottomNavigationComponent.Child>> = childPages(
        source = navigation,
        initialPages = {
            Pages(
                items = ENTRIES,
                selectedIndex = ENTRIES.indexOf(Config.WeatherConfig)
            )
        },
        childFactory = ::child
    )

    fun child(config: Config, componentContext: ComponentContext): BottomNavigationComponent.Child {
        Timber.i("${this::class.simpleName}.child($config, $componentContext)")
        return when (config) {
            is Config.ProfileConfig -> {
                val component = profileRootFactory.create(
                    componentContext = componentContext,
                    user = user
                )
                BottomNavigationComponent.Child.ProfileRoot(component)
            }
            is Config.WeatherConfig -> {
                val component = weatherRootFactory.create(
                    componentContext = componentContext,
                    user = user
                )
                BottomNavigationComponent.Child.WeatherRoot(component)
            }
        }
    }

    sealed interface Config: Parcelable {
        @Parcelize
        data object WeatherConfig: Config

        @Parcelize
        data object ProfileConfig: Config
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("context") componentContext: ComponentContext,
            @Assisted("user") user: User
        ): BottomNavigationComponentImpl
    }

    companion object {
        private val ENTRIES = listOf(Config.WeatherConfig, Config.ProfileConfig)
    }
}