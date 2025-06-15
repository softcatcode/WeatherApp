package com.softcat.database.di

import com.softcat.database.commands.factory.CommandFactory
import com.softcat.database.commands.factory.CommandFactoryInterface
import com.softcat.database.facade.DatabaseFacade
import com.softcat.database.facade.DatabaseProxy
import com.softcat.database.internal.CursorMapper
import com.softcat.database.internal.CursorMapperInterface
import com.softcat.database.internal.sqlExecutor.SQLiteExecutor
import com.softcat.database.internal.sqlExecutor.SQLiteInterface
import com.softcat.database.managers.local.PlotManager
import com.softcat.database.managers.local.PlotManagerImpl
import com.softcat.database.managers.local.region.RegionManager
import com.softcat.database.managers.local.region.RegionManagerImpl
import com.softcat.database.managers.local.weather.WeatherManager
import com.softcat.database.managers.local.weather.WeatherManagerImpl
import com.softcat.database.managers.remote.favourites.FavouritesManager
import com.softcat.database.managers.remote.favourites.FavouritesManagerImpl
import com.softcat.database.managers.remote.user.UsersManager
import com.softcat.database.managers.remote.user.UsersManagerImpl
import dagger.Binds
import dagger.Module

@Module
interface DatabaseModule {

    @ApplicationScope
    @Binds
    fun bindSQLiteExecutor(impl: SQLiteExecutor): SQLiteInterface

    @ApplicationScope
    @Binds
    fun bindDatabase(impl: DatabaseProxy): DatabaseFacade

    @ApplicationScope
    @Binds
    fun bindCommandFactory(impl: CommandFactory): CommandFactoryInterface

    @ApplicationScope
    @Binds
    fun bindUsersManager(impl: UsersManagerImpl): UsersManager

    @ApplicationScope
    @Binds
    fun bindFavouritesManager(impl: FavouritesManagerImpl): FavouritesManager

    @ApplicationScope
    @Binds
    fun bindManagerFactory(impl: RegionManagerImpl): RegionManager

    @ApplicationScope
    @Binds
    fun bindWeatherManagerFactory(impl: WeatherManagerImpl): WeatherManager

    @ApplicationScope
    @Binds
    fun bindCursorMapperInterface(impl: CursorMapper): CursorMapperInterface

    @ApplicationScope
    @Binds
    fun bindPlotManager(impl: PlotManagerImpl): PlotManager
}