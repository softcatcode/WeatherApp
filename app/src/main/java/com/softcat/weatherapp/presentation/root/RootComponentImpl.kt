package com.softcat.weatherapp.presentation.root

import android.os.Parcelable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.softcat.weatherapp.domain.entity.City
import com.softcat.weatherapp.presentation.details.DetailsComponentImpl
import com.softcat.weatherapp.presentation.favourite.FavouritesComponentImpl
import com.softcat.weatherapp.presentation.search.SearchComponentImpl
import com.softcat.weatherapp.presentation.search.SearchOpenReason
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.parcelize.Parcelize

class RootComponentImpl @AssistedInject constructor(
    private val detailsComponentFactory: DetailsComponentImpl.Factory,
    private val favouritesComponentFactory: FavouritesComponentImpl.Factory,
    private val searchComponentFactory: SearchComponentImpl.Factory,
    @Assisted("componentContext") componentContext: ComponentContext
): RootComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, RootComponent.Child>> = childStack(
        source = navigation,
        serializer = null,
        initialConfiguration = Config.Favourite,
        handleBackButton = true,
        childFactory = ::child
    )

    private fun child(config: Config, componentContext: ComponentContext): RootComponent.Child =
        when(config) {
            is Config.Details -> {
                val component = detailsComponentFactory.create(
                    city = config.city,
                    componentContext = componentContext,
                    onBackClickCallback = { navigation.pop() }
                )
                RootComponent.Child.CityDetails(component)
            }

            is Config.Search -> {
                val component = searchComponentFactory.create(
                    componentContext = componentContext,
                    openReason = config.openReason,
                    onBackClickCallback = { navigation.pop() },
                    onOpenForecastCallback = { navigation.push(Config.Details(it)) },
                    onSavedToFavouritesCallback = { navigation.pop() }
                )
                RootComponent.Child.SearchCity(component)
            }

            Config.Favourite -> {
                val component = favouritesComponentFactory.create(
                    componentContext = componentContext,
                    onAddToFavouritesClickCallback = { navigation.push(Config.Search(SearchOpenReason.AddToFavourites)) },
                    onSearchClickCallback = { navigation.push(Config.Search(SearchOpenReason.RegularSearch)) },
                    onCityItemClickedCallback = { navigation.push(Config.Details(it)) }
                )
                RootComponent.Child.Favourites(component)
            }
        }

    sealed interface Config: Parcelable {
        @Parcelize
        data object Favourite: Config

        @Parcelize
        data class Search(val openReason: SearchOpenReason): Config

        @Parcelize
        data class Details(val city: City): Config
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("componentContext") componentContext: ComponentContext
        ): RootComponentImpl
    }
}