package com.softcat.weatherapp.di.components

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.softcat.weatherapp.di.annotations.ApplicationScope
import com.softcat.weatherapp.di.modules.DataModule
import com.softcat.weatherapp.di.modules.PresentationModule
import com.softcat.weatherapp.presentation.MainActivity
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(modules = [DataModule::class, PresentationModule::class])
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