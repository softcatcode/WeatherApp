package com.softcat.weatherapp.di.modules

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import dagger.Module
import dagger.Provides

@Module
class PresentationModule {

    @Provides
    fun bindStoreFactory(): StoreFactory {
        return DefaultStoreFactory()
    }
}