package com.softcat.weatherapp.presentation.root.bottomNavigation.weather

import android.os.Parcelable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.softcat.domain.entity.City
import com.softcat.domain.entity.CurrentWeather
import com.softcat.domain.entity.User
import com.softcat.weatherapp.presentation.calendar.CalendarComponentImpl
import com.softcat.weatherapp.presentation.details.DetailsComponentImpl
import com.softcat.weatherapp.presentation.favourite.FavouritesComponentImpl
import com.softcat.weatherapp.presentation.hourly.HourlyWeatherComponentImpl
import com.softcat.weatherapp.presentation.search.SearchComponentImpl
import com.softcat.weatherapp.presentation.search.SearchOpenReason
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.parcelize.Parcelize
import timber.log.Timber

class WeatherRootImpl @AssistedInject constructor(
    private val detailsComponentFactory: DetailsComponentImpl.Factory,
    private val favouritesComponentFactory: FavouritesComponentImpl.Factory,
    private val searchComponentFactory: SearchComponentImpl.Factory,
    private val calendarComponentFactory: CalendarComponentImpl.Factory,
    private val hourlyWeatherComponentFactory: HourlyWeatherComponentImpl.Factory,
    @Assisted("context") componentContext: ComponentContext,
    @Assisted("user") user: User
): WeatherRootComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, WeatherRootComponent.Child>> = childStack(
        source = navigation,
        initialConfiguration = Config.Favourite(user),
        handleBackButton = true,
        childFactory = ::child
    )

    private fun child(config: Config, componentContext: ComponentContext): WeatherRootComponent.Child {
        Timber.i("${this::class.simpleName}.child($config, $componentContext)")
        return when (config) {
            is Config.Details -> {
                val component = detailsComponentFactory.create(
                    user = config.user,
                    city = config.city,
                    componentContext = componentContext,
                    onBackClickCallback = { navigation.pop() },
                    openCityCalendarCallback = { navigation.push(Config.Calendar(config.city)) },
                    openHourlyWeatherCallback = { weatherList ->
                        navigation.push(Config.HourlyWeather(weatherList))
                    }
                )
                WeatherRootComponent.Child.CityDetails(component)
            }

            is Config.Search -> {
                val component = searchComponentFactory.create(
                    componentContext = componentContext,
                    user = config.user,
                    openReason = config.openReason,
                    onBackClickCallback = {
                        navigation.pop()
                    },
                    onOpenForecastCallback = { user, city ->
                        navigation.push(Config.Details(user, city))
                    },
                    onSavedToFavouritesCallback = { navigation.pop() }
                )
                WeatherRootComponent.Child.SearchCity(component)
            }

            is Config.Favourite -> {
                val component = favouritesComponentFactory.create(
                    user = config.user,
                    componentContext = componentContext,
                    onAddToFavouritesClickCallback = {
                        navigation.push(
                            Config.Search(
                                config.user,
                                SearchOpenReason.AddToFavourites
                            )
                        )
                    },
                    onSearchClickCallback = {
                        navigation.push(Config.Search(config.user, SearchOpenReason.RegularSearch))
                    },
                    onCityItemClickedCallback = { user, city ->
                        navigation.push(Config.Details(user, city))
                    }
                )
                WeatherRootComponent.Child.Favourites(component)
            }

            is Config.Calendar -> {
                val component = calendarComponentFactory.create(
                    componentContext = componentContext,
                    city = config.city,
                    onBackClicked = { navigation.pop() }
                )
                WeatherRootComponent.Child.Calendar(component)
            }

            is Config.HourlyWeather -> {
                val component = hourlyWeatherComponentFactory.create(
                    componentContext = componentContext,
                    hoursWeather = config.hoursWeather,
                    onBackClick = { navigation.pop() }
                )
                WeatherRootComponent.Child.HourlyWeather(component)
            }
        }
    }

    sealed interface Config: Parcelable {
        @Parcelize
        data class Favourite(val user: User): Config

        @Parcelize
        data class Search(
            val user: User,
            val openReason: SearchOpenReason
        ): Config

        @Parcelize
        data class Details(val user: User, val city: City): Config

        @Parcelize
        data class Calendar(val city: City): Config

        @Parcelize
        data class HourlyWeather(val hoursWeather: List<CurrentWeather>): Config
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("context") componentContext: ComponentContext,
            @Assisted("user") user: User
        ): WeatherRootImpl
    }
}