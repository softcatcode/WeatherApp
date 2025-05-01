package com.softcat.data.objectMocks

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.softcat.data.jsonMocks.currentWeatherJson
import com.softcat.data.jsonMocks.searchResultJson
import com.softcat.data.jsonMocks.weatherForecastJson
import com.softcat.data.jsonMocks.weatherHistoryJson
import com.softcat.data.network.api.ApiService
import com.softcat.data.network.dto.CityDto
import com.softcat.data.network.dto.WeatherCurrentDto
import com.softcat.data.network.dto.WeatherForecastDto
import kotlinx.coroutines.runBlocking
import org.mockito.ArgumentMatchers.anyString
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

val apiServiceMock = mock(ApiService::class.java)

private val gson = Gson()
val currentWeatherDto = gson.fromJson(currentWeatherJson, WeatherCurrentDto::class.java)
val weatherForecastDto = gson.fromJson(weatherForecastJson, WeatherForecastDto::class.java)
val weatherHistoryDto = gson.fromJson(weatherHistoryJson, WeatherForecastDto::class.java)
val searchResultDto = gson.fromJson(searchResultJson, JsonArray::class.java).map {
    gson.fromJson(it, CityDto::class.java)
}

fun setupApiServiceMock() = runBlocking {
    whenever(
        apiServiceMock.loadCurrentWeather(anyString())
    ).thenReturn(currentWeatherDto)
    whenever(
        apiServiceMock.loadForecast(anyString(), anyInt())
    ).thenReturn(weatherForecastDto)
    whenever(
        apiServiceMock.loadWeatherHistory(anyString(), anyString(), anyString())
    ).thenReturn(weatherHistoryDto)
    whenever(
        apiServiceMock.searchCity(anyString())
    ).thenReturn(searchResultDto)
}