package com.softcat.weatherapp.di.components

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.softcat.data.implementations.DatastoreRepositoryImpl
import com.softcat.data.implementations.FavouriteRepositoryImpl
import com.softcat.database.di.ApplicationScope
import com.softcat.database.facade.DatabaseFacade
import com.softcat.database.facade.DatabaseFacadeImpl
import com.softcat.database.managers.local.region.RegionManager
import com.softcat.database.managers.local.weather.WeatherManager
import com.softcat.database.managers.remote.user.UsersManager
import com.softcat.weatherapp.di.modules.DataModule
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(modules = [DataModule::class])
interface UnitTestsComponent {

    fun getDatastoreRepository(): DatastoreRepositoryImpl

    fun getFavouriteRepository(): FavouriteRepositoryImpl

    fun getDatabaseImpl(): DatabaseFacadeImpl

    fun getRegionManager(): RegionManager

    fun getWeatherManager(): WeatherManager

    fun getUsersManager(): UsersManager

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context,
            @BindsInstance dataStore: DataStore<Preferences>,
            @BindsInstance database: DatabaseFacade
        ): UnitTestsComponent
    }
}