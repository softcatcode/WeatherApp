package com.softcat.weatherapp.presentation.widgets.hourly

import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import com.softcat.weatherapp.data.implementations.DatastoreRepositoryImpl
import com.softcat.weatherapp.domain.useCases.GetTodayForecastUseCase
import com.softcat.weatherapp.domain.useCases.SearchCityUseCase
import com.softcat.weatherapp.domain.entity.Weather
import com.softcat.weatherapp.domain.useCases.GetCurrentCityNameUseCase
import com.softcat.weatherapp.domain.useCases.GetLastCityFromDatastoreUseCase

class TodayHourlyWeatherWidget(
    private val getForecastUseCase: GetTodayForecastUseCase,
    private val searchUseCase: SearchCityUseCase,
): GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val weatherList = getHourlyWeather(context)
        provideContent {
            Test(weatherList)
        }
    }

    private suspend fun getHourlyWeather(context: Context): List<Weather> {
        val datastoreRepository = DatastoreRepositoryImpl(context)
        val getLastCityNameUseCase = GetLastCityFromDatastoreUseCase(datastoreRepository)
        val cityName = getLastCityNameUseCase() ?: return emptyList()
        val cityId = searchUseCase(cityName).firstOrNull()?.id ?: return emptyList()
        return getForecastUseCase(cityId)
    }
}