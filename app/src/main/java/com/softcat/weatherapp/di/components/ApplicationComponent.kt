package com.softcat.weatherapp.di.components

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.softcat.database.di.ApplicationScope
import com.softcat.database.di.DatabaseModule
import com.softcat.weatherapp.di.modules.DataModule
import com.softcat.weatherapp.di.modules.PresentationModule
import com.softcat.weatherapp.presentation.MainActivity
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(modules = [DataModule::class, PresentationModule::class, DatabaseModule::class])
interface ApplicationComponent {

    fun inject(activity: MainActivity)

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context,
            @BindsInstance dataStore: DataStore<Preferences>
        ): ApplicationComponent
    }
}