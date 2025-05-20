package com.softcat.weatherapp.di.modules

import android.content.Context
import android.location.Geocoder
import com.softcat.data.implementations.AuthorizationRepositoryImpl
import com.softcat.data.implementations.CalendarRepositoryImpl
import com.softcat.data.implementations.DatastoreRepositoryImpl
import com.softcat.data.implementations.FavouriteRepositoryImpl
import com.softcat.data.implementations.Lab9SettingsRepositoryImpl
import com.softcat.data.implementations.LocationRepositoryImpl
import com.softcat.data.implementations.SearchRepositoryImpl
import com.softcat.data.implementations.WeatherRepositoryImpl
import com.softcat.data.network.api.ApiFactory
import com.softcat.data.network.api.ApiService
import com.softcat.database.commands.factory.CommandFactory
import com.softcat.database.commands.factory.CommandFactoryInterface
import com.softcat.database.managers.remote.user.UsersManager
import com.softcat.database.managers.remote.user.UsersManagerImpl
import com.softcat.database.facade.DatabaseFacade
import com.softcat.database.facade.DatabaseFacadeImpl
import com.softcat.database.managers.ManagerFactory
import com.softcat.database.managers.ManagerFactoryInterface
import com.softcat.database.managers.local.region.RegionManager
import com.softcat.database.managers.local.region.RegionManagerImpl
import com.softcat.database.managers.local.region.RegionManagerRemoteImpl
import com.softcat.database.managers.remote.favourites.FavouritesManager
import com.softcat.database.managers.remote.favourites.FavouritesManagerImpl
import com.softcat.domain.interfaces.AuthorizationRepository
import com.softcat.domain.interfaces.CalendarRepository
import com.softcat.domain.interfaces.DatastoreRepository
import com.softcat.domain.interfaces.FavouriteRepository
import com.softcat.domain.interfaces.Lab9SettingsRepository
import com.softcat.domain.interfaces.LocationRepository
import com.softcat.domain.interfaces.SearchRepository
import com.softcat.domain.interfaces.WeatherRepository
import com.softcat.domain.useCases.Lab9SettingsUseCase
import com.softcat.weatherapp.di.annotations.ApplicationScope
import dagger.Binds
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.flow.first
import java.util.Locale

@Module
interface DataModule {

    @ApplicationScope
    @Binds
    fun bindFavouriteRepository(impl: FavouriteRepositoryImpl): FavouriteRepository

    @ApplicationScope
    @Binds
    fun bindSearchRepository(impl: SearchRepositoryImpl): SearchRepository

    @ApplicationScope
    @Binds
    fun bindWeatherRepository(impl: WeatherRepositoryImpl): WeatherRepository

    @ApplicationScope
    @Binds
    fun bindCalendarRepository(impl: CalendarRepositoryImpl): CalendarRepository

    @ApplicationScope
    @Binds
    fun bindDatastoreRepository(impl: DatastoreRepositoryImpl): DatastoreRepository

    @ApplicationScope
    @Binds
    fun bindLocationRepository(impl: LocationRepositoryImpl): LocationRepository

    @ApplicationScope
    @Binds
    fun bindAuthorizationRepository(impl: AuthorizationRepositoryImpl): AuthorizationRepository

    @ApplicationScope
    @Binds
    fun bindLab9SettingsRepository(impl: Lab9SettingsRepositoryImpl): Lab9SettingsRepository

    @ApplicationScope
    @Binds
    fun bindDbFacadeImpl(impl: DatabaseFacadeImpl): DatabaseFacade

    @ApplicationScope
    @Binds
    fun bindUsersManager(impl: UsersManagerImpl): UsersManager

    @ApplicationScope
    @Binds
    fun bindFavouritesManager(impl: FavouritesManagerImpl): FavouritesManager

    @ApplicationScope
    @Binds
    fun bindDatabaseCommandFactory(impl: CommandFactory): CommandFactoryInterface

    @ApplicationScope
    @Binds
    fun bindManagerFactory(impl: ManagerFactory): ManagerFactoryInterface

    companion object {

        @ApplicationScope
        @Provides
        fun provideApiService(): ApiService = ApiFactory.apiService

        @ApplicationScope
        @Provides
        fun provideGeocoder(context: Context) = Geocoder(context, Locale.getDefault())
    }
}