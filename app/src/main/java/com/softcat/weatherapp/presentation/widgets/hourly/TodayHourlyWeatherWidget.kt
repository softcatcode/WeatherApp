package com.softcat.weatherapp.presentation.widgets.hourly

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.glance.GlanceId
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import com.softcat.weatherapp.WeatherApplication
import com.softcat.weatherapp.data.implementations.DatastoreRepositoryImpl
import com.softcat.weatherapp.domain.useCases.GetTodayForecastUseCase
import com.softcat.weatherapp.domain.useCases.SearchCityUseCase
import com.softcat.weatherapp.domain.entity.Weather
import com.softcat.weatherapp.domain.useCases.GetLastCityFromDatastoreUseCase

class TodayHourlyWeatherWidget(
    private val getForecastUseCase: GetTodayForecastUseCase,
    private val searchUseCase: SearchCityUseCase
): GlanceAppWidget() {

    private lateinit var dataStore: DataStore<Preferences>

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        dataStore = (context as WeatherApplication).dataStore
        val weatherList = getHourlyWeather()
        provideContent {
            Test(weatherList)
        }
    }

    private suspend fun getHourlyWeather(): List<Weather> {
        val datastoreRepository = DatastoreRepositoryImpl(dataStore)
        val getLastCityNameUseCase = GetLastCityFromDatastoreUseCase(datastoreRepository)
        val cityName = getLastCityNameUseCase() ?: return emptyList()
        val cityId = searchUseCase(cityName).firstOrNull()?.id ?: return emptyList()
        return getForecastUseCase(cityId)
    }
}