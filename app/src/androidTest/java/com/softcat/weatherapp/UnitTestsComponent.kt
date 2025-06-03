package com.softcat.weatherapp

import android.content.Context
import com.softcat.database.facade.DatabaseFacade
import com.softcat.database.managers.local.region.RegionManager
import com.softcat.database.managers.local.weather.WeatherManager
import com.softcat.database.managers.remote.user.UsersManager
import com.softcat.weatherapp.di.annotations.ApplicationScope
import com.softcat.weatherapp.di.modules.DataModule
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(modules = [DataModule::class])
interface UnitTestsComponent {

    fun getDatabase(): DatabaseFacade

    fun getRegionManager(): RegionManager

    fun getWeatherManager(): WeatherManager

    fun getUsersManager(): UsersManager

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context,
        ): UnitTestsComponent
    }
}