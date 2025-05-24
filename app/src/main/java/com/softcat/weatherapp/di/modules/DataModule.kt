package com.softcat.weatherapp.di.modules

import android.content.Context
import android.location.Geocoder
import com.softcat.data.implementations.AuthorizationRepositoryImpl
import com.softcat.data.implementations.CalendarRepositoryImpl
import com.softcat.data.implementations.DatabaseLoaderRepositoryImpl
import com.softcat.data.implementations.DatastoreRepositoryImpl
import com.softcat.data.implementations.FavouriteRepositoryImpl
import com.softcat.data.implementations.LocationRepositoryImpl
import com.softcat.data.implementations.SearchRepositoryImpl
import com.softcat.data.implementations.WeatherRepositoryImpl
import com.softcat.data.network.api.ApiFactory
import com.softcat.data.network.api.ApiService
import com.softcat.data.network.api.DocsApiFactory
import com.softcat.data.network.api.DocsApiService
import com.softcat.database.commands.factory.CommandFactory
import com.softcat.database.commands.factory.CommandFactoryInterface
import com.softcat.database.managers.remote.user.UsersManager
import com.softcat.database.managers.remote.user.UsersManagerImpl
import com.softcat.database.facade.DatabaseFacade
import com.softcat.database.facade.DatabaseFacadeImpl
import com.softcat.database.internal.CursorMapper
import com.softcat.database.internal.CursorMapperInterface
import com.softcat.database.managers.local.region.RegionManager
import com.softcat.database.managers.local.region.RegionManagerImpl
import com.softcat.database.managers.local.weather.WeatherManager
import com.softcat.database.managers.local.weather.WeatherManagerImpl
import com.softcat.database.managers.remote.favourites.FavouritesManager
import com.softcat.database.managers.remote.favourites.FavouritesManagerImpl
import com.softcat.domain.interfaces.AuthorizationRepository
import com.softcat.domain.interfaces.CalendarRepository
import com.softcat.domain.interfaces.DatabaseLoaderRepository
import com.softcat.domain.interfaces.DatastoreRepository
import com.softcat.domain.interfaces.FavouriteRepository
import com.softcat.domain.interfaces.LocationRepository
import com.softcat.domain.interfaces.SearchRepository
import com.softcat.domain.interfaces.WeatherRepository
import com.softcat.weatherapp.di.annotations.ApplicationScope
import dagger.Binds
import dagger.Module
import dagger.Provides
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
    fun bindDatabaseLoaderRepository(impl: DatabaseLoaderRepositoryImpl): DatabaseLoaderRepository

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
    fun bindManagerFactory(impl: RegionManagerImpl): RegionManager

    @ApplicationScope
    @Binds
    fun bindDatabaseCommandFactory(impl: CommandFactory): CommandFactoryInterface

    @ApplicationScope
    @Binds
    fun bindWeatherManagerFactory(impl: WeatherManagerImpl): WeatherManager

    @ApplicationScope
    @Binds
    fun bindCursorMapperInterface(impl: CursorMapper): CursorMapperInterface

    companion object {

        @ApplicationScope
        @Provides
        fun provideApiService(): ApiService = ApiFactory.apiService

        @ApplicationScope
        @Provides
        fun provideDocsApiService(): DocsApiService = DocsApiFactory.apiService

        @ApplicationScope
        @Provides
        fun provideGeocoder(context: Context) = Geocoder(context, Locale.getDefault())
    }
}