package com.softcat.weatherapp.di.modules

import android.content.Context
import android.location.Geocoder
import com.softcat.data.implementations.AppInfoRepositoryImpl
import com.softcat.data.implementations.new_api.AuthorizationRepositoryImpl
import com.softcat.data.implementations.new_api.CalendarRepositoryImpl
import com.softcat.data.implementations.DatabaseLoaderRepositoryImpl
import com.softcat.data.implementations.DatastoreRepositoryImpl
import com.softcat.data.implementations.new_api.FavouriteRepositoryImpl
import com.softcat.data.implementations.LocationRepositoryImpl
import com.softcat.data.implementations.new_api.SearchRepositoryImpl
import com.softcat.data.implementations.new_api.WeatherRepositoryImpl
import com.softcat.data.network.api.ApiFactory
import com.softcat.data.network.api.ApiService
import com.softcat.data.network.api.DocsApiFactory
import com.softcat.data.network.api.DocsApiService
import com.softcat.data.network.api.NewWeatherApiFactory
import com.softcat.data.network.api.NewWeatherApiService
import com.softcat.database.di.ApplicationScope
import com.softcat.domain.interfaces.AppInfoRepository
import com.softcat.domain.interfaces.AuthorizationRepository
import com.softcat.domain.interfaces.CalendarRepository
import com.softcat.domain.interfaces.DatabaseLoaderRepository
import com.softcat.domain.interfaces.DatastoreRepository
import com.softcat.domain.interfaces.FavouriteRepository
import com.softcat.domain.interfaces.LocationRepository
import com.softcat.domain.interfaces.SearchRepository
import com.softcat.domain.interfaces.WeatherRepository
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
    fun bindAppInfoRepository(impl: AppInfoRepositoryImpl): AppInfoRepository

    companion object {

        @ApplicationScope
        @Provides
        fun provideApiService(): ApiService = ApiFactory.apiService

        @ApplicationScope
        @Provides
        fun provideDocsApiService(): DocsApiService = DocsApiFactory.apiService

        @ApplicationScope
        @Provides
        fun provideNewWeatherApiService(): NewWeatherApiService = NewWeatherApiFactory.apiService

        @ApplicationScope
        @Provides
        fun provideGeocoder(context: Context) = Geocoder(context, Locale.getDefault())
    }
}