package com.softcat.weatherapp.di.modules

import android.content.Context
import com.softcat.weatherapp.data.implementations.CalendarRepositoryImpl
import com.softcat.weatherapp.data.implementations.FavouriteRepositoryImpl
import com.softcat.weatherapp.data.implementations.DatastoreRepositoryImpl
import com.softcat.weatherapp.data.implementations.SearchRepositoryImpl
import com.softcat.weatherapp.data.implementations.WeatherRepositoryImpl
import com.softcat.weatherapp.data.local.db.FavouritesDatabase
import com.softcat.weatherapp.data.network.api.ApiFactory
import com.softcat.weatherapp.data.network.api.ApiService
import com.softcat.weatherapp.di.annotations.ApplicationScope
import com.softcat.weatherapp.domain.interfaces.CalendarRepository
import com.softcat.weatherapp.domain.interfaces.FavouriteRepository
import com.softcat.weatherapp.domain.interfaces.DatastoreRepository
import com.softcat.weatherapp.domain.interfaces.SearchRepository
import com.softcat.weatherapp.domain.interfaces.WeatherRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

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


    companion object {

        @ApplicationScope
        @Provides
        fun provideApiService(): ApiService = ApiFactory.apiService

        @ApplicationScope
        @Provides
        fun provideFavouriteDatabase(context: Context) = FavouritesDatabase.getInstance(context)

        @ApplicationScope
        @Provides
        fun provideFavouriteCitiesDao(db: FavouritesDatabase) = db.getCitiesDao()
    }
}