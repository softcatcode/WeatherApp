package com.softcat.database.di

import android.content.Context
import com.softcat.database.facade.DatabaseFacadeImpl
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(modules = [DatabaseModule::class])
internal interface DatabaseComponent {

    fun getDatabaseImplementation(): DatabaseFacadeImpl

    @Component.Factory
    interface DatabaseComponentFactory {
        fun create(
            @BindsInstance context: Context
        ): DatabaseComponent
    }
}