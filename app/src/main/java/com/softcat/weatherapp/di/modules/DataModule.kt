package com.softcat.weatherapp.di.modules

import android.content.Context
import android.location.Geocoder
import com.softcat.data.implementations.AuthorizationRepositoryImpl
import com.softcat.data.implementations.CalendarRepositoryImpl
import com.softcat.data.implementations.DatastoreRepositoryImpl
import com.softcat.data.implementations.FavouriteRepositoryImpl
import com.softcat.data.implementations.LocationRepositoryImpl
import com.softcat.data.implementations.SearchRepositoryImpl
import com.softcat.data.implementations.WeatherRepositoryImpl
import com.softcat.data.local.db.WeatherDatabase
import com.softcat.data.network.api.ApiFactory
import com.softcat.data.network.api.ApiService
import com.softcat.domain.interfaces.AuthorizationRepository
import com.softcat.domain.interfaces.CalendarRepository
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


    companion object {

        @ApplicationScope
        @Provides
        fun provideApiService(): ApiService = ApiFactory.apiService

        @ApplicationScope
        @Provides
        fun provideFavouriteDatabase(context: Context) = WeatherDatabase.getInstance(context)

        @ApplicationScope
        @Provides
        fun provideFavouriteCitiesDao(db: WeatherDatabase) = db.getCitiesDao()

        @ApplicationScope
        @Provides
        fun provideUsersDao(db: WeatherDatabase) = db.getUsersDao()

        @ApplicationScope
        @Provides
        fun provideGeocoder(context: Context) = Geocoder(context, Locale.getDefault())
    }
}