package com.softcat.weatherapp.presentation.widgets.hourly

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.glance.GlanceId
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import com.softcat.domain.entity.Weather
import com.softcat.domain.useCases.GetLastCityFromDatastoreUseCase
import com.softcat.domain.useCases.GetTodayForecastUseCase
import com.softcat.domain.useCases.SearchCityUseCase
import com.softcat.weatherapp.WeatherApplication

class TodayHourlyWeatherWidget(
    private val getForecastUseCase: GetTodayForecastUseCase,
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
        val datastoreRepository =
            com.softcat.data.implementations.DatastoreRepositoryImpl(dataStore)
        val getLastCityNameUseCase = GetLastCityFromDatastoreUseCase(datastoreRepository)
        val cityName = getLastCityNameUseCase() ?: return emptyList()
        TODO("Get city by name from database and get forecast for it.")
//        val cityId = searchUseCase(cityName).firstOrNull()?.id ?: return emptyList()
//        return getForecastUseCase(cityId)
    }
}