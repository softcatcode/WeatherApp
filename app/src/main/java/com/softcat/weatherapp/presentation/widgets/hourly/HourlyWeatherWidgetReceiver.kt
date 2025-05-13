package com.softcat.weatherapp.presentation.widgets.hourly

import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import com.softcat.domain.useCases.GetTodayForecastUseCase
import com.softcat.domain.useCases.SearchCityUseCase
import com.softcat.data.implementations.SearchRepositoryImpl
import com.softcat.data.implementations.WeatherRepositoryImpl
import com.softcat.data.network.api.ApiFactory

class HourlyWeatherWidgetReceiver: GlanceAppWidgetReceiver() {

    // dependencies
    private val apiService = ApiFactory.apiService
    private val weatherRepository = WeatherRepositoryImpl(apiService)
    //private val searchRepository = SearchRepositoryImpl(apiService)
    private val getForecastUseCase = GetTodayForecastUseCase(weatherRepository)
    //private val searchUseCase = SearchCityUseCase(searchRepository)


    override val glanceAppWidget: GlanceAppWidget = TodayHourlyWeatherWidget(
        getForecastUseCase,
    )
}