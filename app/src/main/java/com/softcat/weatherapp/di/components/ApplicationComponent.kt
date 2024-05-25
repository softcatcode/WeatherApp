package com.softcat.weatherapp.di.components

import android.content.Context
import com.softcat.weatherapp.di.annotations.ApplicationScope
import com.softcat.weatherapp.di.modules.DataModule
import dagger.BindsInstance
import dagger.Component
@ApplicationScope
@Component(modules = [DataModule::class])
interface ApplicationComponent {

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context
        ): ApplicationComponent
    }
}