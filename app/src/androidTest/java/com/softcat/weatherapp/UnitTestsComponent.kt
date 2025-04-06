package com.softcat.weatherapp

import android.content.Context
import com.softcat.weatherapp.data.local.db.FavouriteCitiesDao
import com.softcat.weatherapp.di.annotations.ApplicationScope
import com.softcat.weatherapp.di.modules.DataModule
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(modules = [DataModule::class])
interface UnitTestsComponent {

    fun getFavouriteCitiesDao(): FavouriteCitiesDao

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context,
        ): UnitTestsComponent
    }
}