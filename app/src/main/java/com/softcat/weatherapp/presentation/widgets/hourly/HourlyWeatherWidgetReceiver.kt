package com.softcat.weatherapp.presentation.widgets.hourly

import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import com.softcat.weatherapp.data.implementations.DatastoreRepositoryImpl
import com.softcat.weatherapp.data.implementations.SearchRepositoryImpl
import com.softcat.weatherapp.data.implementations.WeatherRepositoryImpl
import com.softcat.weatherapp.data.network.api.ApiFactory
import com.softcat.weatherapp.domain.useCases.GetCurrentCityNameUseCase
import com.softcat.weatherapp.domain.useCases.GetLastCityFromDatastoreUseCase
import com.softcat.weatherapp.domain.useCases.GetTodayForecastUseCase
import com.softcat.weatherapp.domain.useCases.SearchCityUseCase

class HourlyWeatherWidgetReceiver: GlanceAppWidgetReceiver() {

    // dependencies
    private val apiService = ApiFactory.apiService
    private val weatherRepository = WeatherRepositoryImpl(apiService)
    private val searchRepository = SearchRepositoryImpl(apiService)
    private val getForecastUseCase = GetTodayForecastUseCase(weatherRepository)
    private val searchUseCase = SearchCityUseCase(searchRepository)


    override val glanceAppWidget: GlanceAppWidget = TodayHourlyWeatherWidget(
        getForecastUseCase,
        searchUseCase,
    )
}